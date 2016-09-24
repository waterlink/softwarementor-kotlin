package softwarementor.mentor

import softwarementor.Context
import softwarementor.mentor.PresentedMentor

class PresentAvailableMentorsForLanguage {
    fun presentAvailableMentorsFor(language: String) =
            Context.gateway.findMentorsByLanguage(language).map {
                PresentedMentor(it.id, it.language)
            }
}