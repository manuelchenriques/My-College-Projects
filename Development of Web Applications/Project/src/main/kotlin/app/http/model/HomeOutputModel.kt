package app.http.model

data class HomeOutputModel(
    val credits: String,
)

data class AboutOutputModel(
    val developers:List<Developers>,
    val from:String,
    val course:String,
    val professor:String
)
data class Developers(val name:String, val number:Int, val publicMail:String, val gitPage:String)
