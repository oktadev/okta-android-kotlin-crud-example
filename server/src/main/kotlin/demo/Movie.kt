package demo

import javax.persistence.*

@Entity
data class Movie(
		@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
val Id: Long,
		val name: String)
