package customer

import groovy.transform.*

@EqualsAndHashCode
@ToString(includeNames=true)
class Customer {
	//Should be final
	def id
	def name
	def email

	static Customer createNewCustomer(name, email) {
		new Customer(id: UUID.randomUUID(),
		name: name,
		email: email)
	}
}