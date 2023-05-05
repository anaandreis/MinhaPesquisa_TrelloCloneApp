package com.anaandreis.minhapesquisa_trellocloneapp.projectsHome.data.models

import java.time.DayOfWeek
import java.time.LocalDate
import java.time.LocalTime

data class ArchivedProjects(
        val projectName : String,
        val description : String,
        val image: String,
        val deadline: LocalDate,
        val members: MutableList<String>)
    {
        data class testGroup(
            val testId: Int,
            val groupName: String,
            val groupDescription: String,
            val numberOfSamples: Int
        )
        data class Sample(
            val testId: Int,
            val sampleDescription: String,
            val sampleImage: String,
            val sampleDate: LocalDate,
            val sampleTime: LocalTime
        )
    }
