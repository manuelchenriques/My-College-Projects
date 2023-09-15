package app
import app.http.pipeline.AuthenticationInterceptor
import app.http.pipeline.Converters.FleetSchemaConverter
import app.http.pipeline.Converters.ShipInputModelConverter
import app.http.pipeline.UserArgumentResolver
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.jdbc.DataSourceBuilder
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Primary
import org.springframework.format.FormatterRegistry
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.stereotype.Component
import org.springframework.web.method.support.HandlerMethodArgumentResolver
import org.springframework.web.servlet.config.annotation.InterceptorRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer
import java.time.Instant
import javax.sql.DataSource

@SpringBootApplication
class BattleShipsApplication {

    @Bean
    fun passwordEncoder() = BCryptPasswordEncoder()
    @Bean
    fun clock() = object : Clock {
        override fun now() = Instant.now()
    }

}

@Configuration
@Component
class setDataSource{
    @ConfigurationProperties(prefix = "spring.datasource")
    @Bean
    @Primary
    fun dataSource(): DataSource? {
        val url = System.getenv("JDBC_DATABASE_URL")
            ?: "jdbc:postgresql://localhost:5432/postgres?user=postgres&password=postgres"

        return DataSourceBuilder
            .create()
            .url(url)
            .build()
    }
}
@Configuration
class PipelineConfigurer(
    val authenticationInterceptor: AuthenticationInterceptor,
    val userArgumentResolver: UserArgumentResolver,
) : WebMvcConfigurer {

    override fun addInterceptors(registry: InterceptorRegistry) {
        registry.addInterceptor(authenticationInterceptor)
    }

    override fun addArgumentResolvers(resolvers: MutableList<HandlerMethodArgumentResolver>) {
        resolvers.add(userArgumentResolver)
    }


    override fun addFormatters(registry: FormatterRegistry) {
        registry.addConverter(FleetSchemaConverter() )
        registry.addConverter(ShipInputModelConverter())
    }
}

fun main(args: Array<String>) {
    runApplication<BattleShipsApplication>(*args)
}