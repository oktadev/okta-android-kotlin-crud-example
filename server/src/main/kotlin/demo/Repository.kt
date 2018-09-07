package demo

import org.springframework.data.repository.CrudRepository

interface ItemRepository : CrudRepository<Movie, Long>
