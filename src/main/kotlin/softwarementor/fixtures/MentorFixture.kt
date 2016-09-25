package softwarementor.fixtures

import softwarementor.mentor.Mentor
import softwarementor.mentor.MentorGateway
import softwarementor.mentor.PresentAvailableMentorsForLanguage
import softwarementor.mentor.PresentedMentor
import java.util.*

interface MentorFixture {
    val mentorGateway: MentorGateway
    val presentAvailableMentorsForLanguage: PresentAvailableMentorsForLanguage
    var availableMentors: List<PresentedMentor>?

    @AcceptanceMethod
    fun givenThereIsAMentorWithNameWhoKnows(name: String, language: String): Boolean {
        mentorGateway.save(Mentor(name, language))
        return mentorGateway.findAll().last().name == name
    }

    @AcceptanceMethod
    fun givenThereAreNoMentors(): Boolean {
        ArrayList<Mentor>(mentorGateway.findAll())
                .forEach { mentorGateway.delete(it) }
        return mentorGateway.findAll().count() == 0
    }

    @AcceptanceMethod
    fun whenPresentingAvailableMentorsFor(language: String): Boolean {
        availableMentors = presentAvailableMentorsForLanguage.presentAvailableMentorsFor(language)
        return availableMentors!!.all { it.language == language }
    }

    @AcceptanceMethod
    fun availableMentorsCount() =
            availableMentors?.count()
}