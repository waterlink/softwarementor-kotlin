package softwarementor.fixtures

import softwarementor.Gateway
import softwarementor.mentor.Mentor
import softwarementor.mentor.PresentAvailableMentorsForLanguage
import softwarementor.mentor.PresentedMentor
import java.util.*

interface MentorFixture {
    val gateway: Gateway
    val presentAvailableMentorsForLanguage: PresentAvailableMentorsForLanguage
    var availableMentors: List<PresentedMentor>?

    @AcceptanceMethod
    fun givenThereIsAMentorWhoKnows(language: String): Boolean {
        gateway.save(Mentor(language))
        return gateway.findAllMentors().last().language == language
    }

    @AcceptanceMethod
    fun givenThereAreNoMentors(): Boolean {
        ArrayList<Mentor>(gateway.findAllMentors()).forEach { gateway.delete(it) }
        return gateway.findAllMentors().count() == 0
    }

    @AcceptanceMethod
    fun whenPresentingAvailableMentorsFor(language: String): Boolean {
        availableMentors = presentAvailableMentorsForLanguage.presentAvailableMentorsFor(language)
        return availableMentors!!.all { it.language == language }
    }

    @AcceptanceMethod
    fun thenAvailableMentorsCountIs(expectedCount: Int) =
            availableMentors?.count() == expectedCount
}