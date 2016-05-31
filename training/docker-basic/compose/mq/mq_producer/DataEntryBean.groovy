@Grab('org.apache.camel:camel-bindy:2.16.0')
import org.apache.camel.dataformat.bindy.annotation.CsvRecord
import org.apache.camel.dataformat.bindy.annotation.DataField

import javax.xml.bind.annotation.XmlAccessType
import javax.xml.bind.annotation.XmlAccessorType
import javax.xml.bind.annotation.XmlAttribute
import javax.xml.bind.annotation.XmlRootElement
import java.time.LocalDateTime

/**
 * A basic bean for handling incoming CSV data.
 * Provides a mapping to XML.
 */
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
@CsvRecord(separator = ',', skipFirstLine = false)
class DataEntryBean {
    static random = new Random()

    @XmlAttribute
    @DataField(pos = 1, required = true, trim = true)
    final String timestamp

    @XmlAttribute
    @DataField(pos = 2, required = true, trim = true)
    final Integer value

    DataEntryBean() {
        timestamp = LocalDateTime.now().toString()
        value = random.nextInt(1000)
    }

    String toString() {
        "$timestamp,$value".toString()
    }
}
