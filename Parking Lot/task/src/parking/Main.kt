package parking

data class Car(val registrationNumber: String = "", var color: String = "")

class ParkingLot {
    var spots: MutableList<Car?>? = null

    fun createLot(index: Int) {
        if (index <= 0) {
            println("Index must be positive.")
            return
        }
        spots = MutableList(index) { null }
        println("Created a parking lot with $index spots.")
    }

    fun parkCar(car: Car) {
        // nothing to do if there is no parking lot
        if (spots == null) {
            println("Sorry, a parking lot has not been created.")
            return
        }

        // park the car in the first available spot
        // get the first null index
        val firstAvailable = spots?.indexOfFirst { it == null } ?: -1

        if (firstAvailable == -1) {
            println("Sorry, the parking lot is full.")
            return
        }

        spots?.set(firstAvailable, car)
        println("${car.color} car parked in spot ${firstAvailable + 1}.")
    }

    fun vacateSpot(index: Int) {
        // nothing to do if there is no parking lot
        if (spots == null) {
            println("Sorry, a parking lot has not been created.")
            return
        }

        if (index >= 0 && index < (spots?.size ?: 0)) {
            if (spots?.get(index) != null) {
                spots?.set(index, null)
                println("Spot ${index + 1} is free.")
            } else {
                println("There is no car in spot ${index + 1}.")
            }
        } else {
            println("Index out of range.")
        }
    }

    fun showStatus() {
        // nothing to do if there is no parking lot
        if (spots == null) {
            println("Sorry, a parking lot has not been created.")
            return
        }

        val nonNullSpots = spots?.mapNotNull { it }
        if (nonNullSpots?.isEmpty() == true) {
            println("Parking lot is empty.")
        } else {
            spots
                ?.forEachIndexed { index, car -> if (car != null)
                    println("${index + 1} ${car.registrationNumber} ${car.color}") }
        }
    }

    fun queryCommand(queryType: String, input: String) {
        if (spots == null) {
            println("Sorry, a parking lot has not been created.")
            return
        }

        val nonNullSpots = spots?.mapNotNull { it }
        if (nonNullSpots?.isEmpty() == true) {
            println("Parking lot is empty.")
        } else {
            when (queryType) {
                "reg_by_color" -> {
                    val results = spots
                        ?.filter { it?.color.equals(input, true) }
                        ?.joinToString(separator = ", ") { it?.registrationNumber ?: "" }

                    println(if (results.isNullOrEmpty()) "No cars with color $input were found." else results)
                }
                "spot_by_color" -> {
                    val results = spots
                        ?.mapIndexedNotNull { index, car -> if (car?.color.equals(input, true)) index + 1 else null}
                        ?.joinToString(", ")

                    println(if (results.isNullOrEmpty()) "No cars with color $input were found." else results)
                }
                "spot_by_reg" -> {
                    val results = spots
                        ?.mapIndexedNotNull { index, car -> if (car?.registrationNumber.equals(input)) index + 1 else null}
                        ?.joinToString(", ")

                    println(if (results.isNullOrEmpty()) "No cars with registration number $input were found." else results)
                }
            }
        }
    }
}

fun isInputValid(inputList: List<String>): Boolean {
    // nothing to parse
    if (inputList.isEmpty()) {
        return false
    }

    // first argument not a keyword
    if (inputList[0] !in listOf("create", "park", "leave", "status", "reg_by_color", "spot_by_color", "spot_by_reg", "exit")) {
        return false
    }

    // check the number of arguments
    when (inputList[0]) {
        "create" -> if (inputList.size < 2) return false
        "park" -> if (inputList.size < 3) return false
        "reg_by_color" -> if (inputList.size < 2) return false
        "spot_by_color" -> if (inputList.size < 2) return false
        "spot_by_reg" -> if (inputList.size < 2) return false
        "leave" -> if (inputList.size < 2) return false
    }

    // valid otherwise
    return true
}

fun main() {
    // create an empty parking lot
    val parkingLot = ParkingLot()

    while (true) {
        // get user input
        val userInput = readln().split(" ")

        if (isInputValid(userInput)) {
            when (userInput[0]) {
                "create" -> parkingLot.createLot(userInput[1].toInt())
                "park" -> parkingLot.parkCar(Car(userInput[1], userInput[2]))
                "leave" -> parkingLot.vacateSpot(userInput[1].toInt() - 1)
                "status" -> parkingLot.showStatus()
                "reg_by_color", "spot_by_color", "spot_by_reg" -> parkingLot.queryCommand(userInput[0], userInput[1])
                "exit" -> break
                else -> println("Invalid input.")
            }
        } else {
            println("Invalid input")
        }
    }
}
