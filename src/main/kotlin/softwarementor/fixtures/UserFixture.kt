package softwarementor.fixtures

import softwarementor.user.CurrentUserRepository

interface UserFixture {
    val currentUserRepository: CurrentUserRepository

    @AcceptanceMethod
    fun givenUserIsAGuest(): Boolean {
        currentUserRepository.assumeGuest()
        return currentUserRepository.isGuest()
    }

    @AcceptanceMethod
    fun givenUserIsAMentee(): Boolean {
        currentUserRepository.assumeMentee()
        return currentUserRepository.isMentee()
    }
}

