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
    fun givenThereIsAMentorWithNameWhoKnows(name: String, language: String): Boolean {
        gateway.save(Mentor(name, "sampleEmail@example.org", "NO_PASSWORD", language))
        return gateway.findAllMentors().last().name == name
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