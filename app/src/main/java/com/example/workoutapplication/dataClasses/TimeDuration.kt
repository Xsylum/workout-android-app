package com.example.workoutapplication.dataClasses

class TimeDuration(
    var hours: Int = 0,
    var minutes: Int = 0,
    var seconds: Int = 0, // milliseconds, microseconds, etc. (anything after the period)
    var fractionalSeconds: String = "") {

    init {
        if (hours < 0) {
            throw IllegalArgumentException("hours must be 0 or positive!")
        } else if (minutes < 0 || minutes > 59) {
            throw IllegalArgumentException("Minutes must be between 0 and 59!")
        } else if (seconds < 0 || seconds > 59) {
            throw IllegalArgumentException("Seconds must be between 0 and 59!")
        } else if (fractionalSeconds.toInt() < 0) {
            throw IllegalArgumentException("Cannot have a fraction of a second below 0!")
        }
    }

    companion object {
        /**
         * Creates a TimeDuration using a parsed string obtained from TimeDuration.toString()
         */
        fun fromParsedString(parsedString: String): TimeDuration {
            // splitting the parsed string delegated to splitParsedString()
            val parsedStringInfo = splitParsedString(parsedString)
            val timeValues = parsedStringInfo.first
            val hasFractionalSeconds = parsedStringInfo.second

            if (timeValues.size !in 1..4) {
                throw IllegalArgumentException("Passed a pair which contains faulty time data:" +
                        "$timeValues")
            }

            when (timeValues.size) {
                4 -> {
                    return TimeDuration(hours = timeValues[0].toInt(),
                                        minutes = timeValues[1].toInt(),
                                        seconds = timeValues[2].toInt(),
                                        fractionalSeconds = timeValues[3])
                }
                3 -> {
                    if (hasFractionalSeconds) {
                        return TimeDuration(minutes = timeValues[0].toInt(),
                                            seconds = timeValues[1].toInt(),
                                            fractionalSeconds = timeValues[2])
                    } else {
                        return TimeDuration(hours = timeValues[0].toInt(),
                                            minutes = timeValues[1].toInt(),
                                            seconds = timeValues[2].toInt())
                    }
                }
                2 -> {
                    if (hasFractionalSeconds) {
                        return TimeDuration(seconds = timeValues[1].toInt(),
                                            fractionalSeconds = timeValues[2])
                    } else {
                        return TimeDuration(minutes = timeValues[1].toInt(),
                                            seconds = timeValues[2].toInt())
                    }

                }
                else -> {
                    return TimeDuration(seconds = timeValues[0].toInt())
                }
            }
        }

        /**
         * Helper method for splitting up a string from companion TimeDuration.fromParsedString
         *
         * @return Pair containing ArrayList of time values, and Boolean stating if the
         *          ArrayList contains a fractionalSeconds value
         */
        private fun splitParsedString(parsedString: String): Pair<ArrayList<String>, Boolean> {
            //TODO: Ensure the parsedString has appropriate formatting!!!!
            val splitTime = ArrayList<String>()
            var hasFractionalSeconds = false

            if (parsedString.contains(":")) { // contains time larger than seconds
                splitTime.addAll(parsedString.split(":"))

                // time contains seconds & milliseconds
                if (splitTime[splitTime.size - 1].contains(".")) { // contains fractional seconds
                    // split up and then add the seconds and fractional seconds
                    val secondsAndFractional = splitTime.removeAt(splitTime.size - 1)
                    val splitSecondsAndFractional = secondsAndFractional.split(".")
                    splitTime.addAll(splitSecondsAndFractional)

                    hasFractionalSeconds = true
                }

            } else if (parsedString.contains(".")) { // contains fractional seconds
                // split up and then add the seconds and fractional seconds
                val secondsAndFractional = splitTime.removeAt(splitTime.size - 1)
                val splitSecondsAndFractional = secondsAndFractional.split(".")
                splitTime.addAll(splitSecondsAndFractional)

                hasFractionalSeconds = true
            }

            return Pair(splitTime, hasFractionalSeconds)
        }
    }

    override fun toString(): String {
        val stringBuilder = StringBuilder()

        if (hours == 0) {
                if (minutes == 0) {
                    stringBuilder.append("$seconds")
                    if (fractionalSeconds != "") stringBuilder.append(".$fractionalSeconds")
                } else {
                    stringBuilder.append("$minutes:")
                    if (seconds in 0..9) stringBuilder.append("0")
                    stringBuilder.append("$seconds")
                    if (fractionalSeconds != "") stringBuilder.append(".$fractionalSeconds")
                }
        }
        else {
            stringBuilder.append("$hours:")
            if (minutes in 0..9) stringBuilder.append("0")
            stringBuilder.append("$minutes:")
            if (seconds in 0..9) stringBuilder.append("0")
            stringBuilder.append("$seconds")
            if (fractionalSeconds != "") stringBuilder.append(".$fractionalSeconds")
        }

        return stringBuilder.toString()
    }
}