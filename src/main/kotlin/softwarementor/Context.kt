package softwarementor

import softwarementor.mentee.CurrentMenteeRepository
import softwarementor.Gateway
import softwarementor.mentor.CurrentMentorRepository

object Context {
    lateinit var gateway: Gateway
    lateinit var currentMenteeRepository: CurrentMenteeRepository
    lateinit var currentMentorRepository: CurrentMentorRepository
}