package com.example.repository

import com.example.EnvironmentConfig
import com.example.neo4j.App
import com.example.neo4j.User
import org.neo4j.ogm.config.Configuration
import org.neo4j.ogm.session.Session
import org.neo4j.ogm.session.SessionFactory
import java.lang.Exception

private const val DOMAIN_OBJECTS_PACKAGE = "com.example"

class Neo4jRepositoryImpl : Neo4jRepository {

    private var sessionFactory: SessionFactory? = null
    private var sessionIsOpen = false

    private val datasource by lazy {
        var session: Session? = null
        try {
            session = sessionFactory!!.openSession()
            sessionIsOpen = true
        } catch (e: Exception) {
            sessionIsOpen = false
        }
        session
    }

    init {
        val conf = EnvironmentConfig()
        val configuration = Configuration.Builder()
            .uri(conf.neo4jUrl)
            .credentials(conf.neo4jUsername, conf.neo4jPassword)
            .build()
        sessionFactory = SessionFactory(configuration, DOMAIN_OBJECTS_PACKAGE)
    }

    override suspend fun test() = transaction {
        User(displayName = "Maks", country="ua").also {
            save(it)
        }
    }

    override suspend fun getUser(telegramId: Long): User? = transaction {
        val queryStr = "MATCH(user:User{telegramId: $telegramId}) RETURN user"
        query(User::class.java, queryStr, HashMap<String, String>()).firstOrNull()
//        query(User::class.java, queryStr, HashMap<String, String>()).firstOrNull()
//        val queryString =
//            "MATCH(u:User{username: {username}})-[:BELONGS_TO]->(c:ChatGroup)<-[:BELONGS_TO]-(u2:User) RETURN u2"
//        query(UserNode::class.java, queryString, mapOf("username" to username)).map {
//            it.toDomainModel()
//        }
    }

    override suspend fun createUser(user: User) = transaction {
        User(id = user.id, displayName = user.displayName, country=user.country).also {
            save(it)
        }
        try {
            val queryStr = "CREATE CONSTRAINT x ON (user:User) ASSERT user.id IS UNIQUE"
            query(queryStr, HashMap<String, String>())
        } catch (ex: Exception) {
            //ignore error that this constraint has been already exist
        }
        true
    }

    override suspend fun removeUser(telegramId: Long) = transaction {
        val queryStr = "MATCH(user:User{telegramId: '$telegramId'}) DETACH DELETE user RETURN user"
        query(queryStr, HashMap<String, String>())
        true
    }

    override suspend fun addApp(telegramId: Long, packageName: String): Boolean {
        transaction {
            val queryStr = "MERGE(user:User{id: $telegramId}) " +
                    "MERGE(app:App{packageName: '$packageName'}) " +
                    "MERGE(user)-[:HAS]->(app)" +
                    "RETURN user"
            query(queryStr, HashMap<String, String>())
        }
        return true
    }

    override suspend fun removeApp(telegramId: Long, packageName: String): Boolean {
        transaction {
            val queryStr = "MATCH (any:User)-[rel:HAS]->(app:App{packageName:'$packageName'})\n" +
                    "WITH any, rel, app\n" +
                    "OPTIONAL MATCH (user:User{id:$telegramId})-[relForDelete]->(app)\n" +
                    "DELETE relForDelete\n" +
                    "WITH size(()-->(app)) AS count, app, any, rel\n" +
                    "WHERE count = 0\n" +
                    "DELETE app\n" +
                    "RETURN app"
            query(queryStr, HashMap<String, String>())
        }
        return true
    }

    override suspend fun getApps(telegramId: Long): List<App> = transaction {
        val queryStr = "MATCH (user:User{id:$telegramId})-[]->(app:App) RETURN app"
        query(App::class.java, queryStr, HashMap<String, String>()).toList()
    }

    override suspend fun login(me: Long, myFriends: List<Long>): List<User> {
        val users = ArrayList(myFriends)
        users.add(0, me)
        return getUsers(users)
    }

    override suspend fun getUsers(users: List<Long>): List<User> = transaction {
        val query = "MATCH (user:User)\n" +
                "WHERE user.id IN [${users.joinToString(",")}]\n" +
                "OPTIONAL MATCH (user)-[rel]->(app:App)\n" +
                "RETURN user, rel, app"
        query(User::class.java, query, HashMap<String, String>()).toList()
    }

    override suspend fun getWorldApps(): List<App> = transaction {
        val query = "MATCH (app:App)<-[rel:HAS]-(user:User)\n" +
                "RETURN app, rel, user"
        query(App::class.java, query, HashMap<String, String>()).toList()
    }

    override suspend fun getCountryApps(country: String): List<App> = transaction {
        val query = "MATCH (app:App)<-[rel:HAS]-(user:User{country: '$country'})\n" +
                "RETURN app, rel, user"
        query(App::class.java, query, HashMap<String, String>()).toList()
    }

    override suspend fun getFriendsApps(users: List<Long>): List<App> = transaction {
        val query = "MATCH (user:User)\n" +
                "WHERE user.id IN [${users.joinToString(",")}]\n" +
                "OPTIONAL MATCH (user)-[rel]->(app:App)\n" +
                "RETURN user, rel, app"
        query(App::class.java, query, HashMap<String, String>()).toList()
    }

    private fun <T> transaction(op: Session.() -> T) =
        datasource!!.op()

}