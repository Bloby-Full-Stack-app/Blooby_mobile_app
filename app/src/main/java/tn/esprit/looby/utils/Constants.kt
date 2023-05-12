package tn.esprit.looby.utils

object Constants {
    private const val PORT = "5000"

    /// ----- Emulator ----- ///
    const val BASE_URL = "http://10.0.2.2:$PORT/"

    /// ------ Device ------ ///
    //const val BASE_URL = "http://192.168.1.233:$PORT/"

    /// ------ Heroku ------ ///
    //const val BASE_URL = "https://chicky-app.herokuapp.com/"

    const val BASE_URL_IMAGES = BASE_URL + "images-files/"
    const val BASE_URL_VIDEOS = BASE_URL + "videos-files/"
    const val BASE_URL_MUSIC = BASE_URL + "music-files/"

    const val SHARED_PREF_SESSION = "SESSION"
}