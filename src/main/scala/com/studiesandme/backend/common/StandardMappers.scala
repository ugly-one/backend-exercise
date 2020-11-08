package com.studiesandme.backend.common

import java.sql.{Date, Timestamp}
import org.joda.time.{DateTime, LocalDate}

//import scala.language.postfixOps

trait StandardMappers {
  this: DBComponent =>

  import driver.api._

  implicit lazy val dateMapper = MappedColumnType.base[org.joda.time.DateTime, java.sql.Timestamp](
    { dateTime =>
      new Timestamp(dateTime.getMillis)
    }, { time =>
      new DateTime(time.getTime)
    },
  )

  implicit lazy val localDateMapper = MappedColumnType.base[org.joda.time.LocalDate, java.sql.Date](
    // Converion based on: https://stackoverflow.com/a/8992524/1278218
    { localDate =>
      new Date(localDate.toDateTimeAtStartOfDay.getMillis)
    }, { date =>
      new LocalDate(date.toString)
    },
  )
}
