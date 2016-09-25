package softwarementor.mentor

import softwarementor.Context
import softwarementor.mentor.PresentedMentor

class PresentAvailableMentorsForLanguage(private val context: Context) {
    fun presentAvailableMentorsFor(language: String) =
            context.mentorGateway.findByLanguage(language).map {
                PresentedMentor(it.id, it.language)
            }
}