package app.http

import app.http.infra.siren
import app.http.model.AboutOutputModel
import app.http.model.Developers
import app.http.model.HomeOutputModel
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController


@RestController
class HomeController {
    @GetMapping(Uris.HOME)
    fun getHome() = siren(HomeOutputModel("Made for learning purposes")) {
        link(Uris.home(), Rels.HOME)
    }
    @GetMapping(Uris.ABOUT)
    fun getAbout() = AboutOutputModel(
                        developers = listOf(
                            Developers("Miguel Neves",47230,"A47230@alunos.isel.pt","https://github.com/yeetus-deletus-neves"),
                            Developers("Pedro Batista",47246,"A47246@alunos.isel.pt","https://github.com/bigskydiver"),
                            Developers("Manuel Henriques",47202,"A47202@alunos.isel.pt","https://github.com/manuelchenriques")),
                        from = "Instituto Superior de Engenharia de Lisboa, Licenciatura de Engenharia Informática e de Computadores",
                        course = "DAW - Desenvolvimento de Aplicações na Web",
                        professor = "Engenheiro Filipe Freitas")
}
