package softwarementor.mentor

import softwarementor.Context
import softwarementor.mentor.PresentedMentor

class PresentAvailableMentorsForLanguage {
    fun presentAvailableMentorsFor(language: String) =
            Context.mentorGateway.findByLanguage(language).map {
                PresentedMentor(it.id, it.language)
            }
}