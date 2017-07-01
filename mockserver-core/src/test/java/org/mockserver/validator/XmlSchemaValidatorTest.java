package org.mockserver.validator;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.Mock;
import org.slf4j.Logger;
import org.xml.sax.SAXParseException;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.mockito.MockitoAnnotations.initMocks;
import static org.mockserver.character.Character.NEW_LINE;

/**
 * @author jamesdbloom
 */
public class XmlSchemaValidatorTest {

    @Rule
    public final ExpectedException exception = ExpectedException.none();

    public static final String XML_SCHEMA = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" + NEW_LINE +
            "<xs:schema xmlns:xs=\"http://www.w3.org/2001/XMLSchema\" elementFormDefault=\"qualified\" attributeFormDefault=\"unqualified\">" + NEW_LINE +
            "    <!-- XML Schema Generated from XML Document on Wed Jun 28 2017 21:52:45 GMT+0100 (BST) -->" + NEW_LINE +
            "    <!-- with XmlGrid.net Free Online Service http://xmlgrid.net -->" + NEW_LINE +
            "    <xs:element name=\"notes\">" + NEW_LINE +
            "        <xs:complexType>" + NEW_LINE +
            "            <xs:sequence>" + NEW_LINE +
            "                <xs:element name=\"note\" maxOccurs=\"unbounded\">" + NEW_LINE +
            "                    <xs:complexType>" + NEW_LINE +
            "                        <xs:sequence>" + NEW_LINE +
            "                            <xs:element name=\"to\" type=\"xs:string\"></xs:element>" + NEW_LINE +
            "                            <xs:element name=\"from\" type=\"xs:string\"></xs:element>" + NEW_LINE +
            "                            <xs:element name=\"heading\" type=\"xs:string\"></xs:element>" + NEW_LINE +
            "                            <xs:element name=\"body\" type=\"xs:string\"></xs:element>" + NEW_LINE +
            "                        </xs:sequence>" + NEW_LINE +
            "                    </xs:complexType>" + NEW_LINE +
            "                </xs:element>" + NEW_LINE +
            "            </xs:sequence>" + NEW_LINE +
            "        </xs:complexType>" + NEW_LINE +
            "    </xs:element>" + NEW_LINE +
            "</xs:schema>";

    @Mock
    protected Logger logger;

    @Before
    public void createMocks() {
        initMocks(this);
    }

    @Test
    public void shouldMatchXml() {
        assertThat(new XmlSchemaValidator(XML_SCHEMA).isValid("<?xml version=\"1.0\" encoding=\"utf-8\"?>" + NEW_LINE +
                "<notes>" + NEW_LINE +
                "    <note>" + NEW_LINE +
                "        <to>Bob</to>" + NEW_LINE +
                "        <from>Bill</from>" + NEW_LINE +
                "        <heading>Reminder</heading>" + NEW_LINE +
                "        <body>Buy Bread</body>" + NEW_LINE +
                "    </note>" + NEW_LINE +
                "    <note>" + NEW_LINE +
                "        <to>Jack</to>" + NEW_LINE +
                "        <from>Jill</from>" + NEW_LINE +
                "        <heading>Reminder</heading>" + NEW_LINE +
                "        <body>Wash Shirts</body>" + NEW_LINE +
                "    </note>" + NEW_LINE +
                "</notes>"), is(""));
    }

    @Test
    public void shouldHandleXmlMissingRequiredFields() {
        // then
        assertThat(new XmlSchemaValidator(XML_SCHEMA).isValid( "<?xml version=\"1.0\" encoding=\"utf-8\"?>" + NEW_LINE +
                "<notes>" + NEW_LINE +
                "    <note>" + NEW_LINE +
                "        <to>Bob</to>" + NEW_LINE +
                "        <heading>Reminder</heading>" + NEW_LINE +
                "        <body>Buy Bread</body>" + NEW_LINE +
                "    </note>" + NEW_LINE +
                "    <note>" + NEW_LINE +
                "        <to>Jack</to>" + NEW_LINE +
                "        <from>Jill</from>" + NEW_LINE +
                "        <heading>Reminder</heading>" + NEW_LINE +
                "        <body>Wash Shirts</body>" + NEW_LINE +
                "    </note>" + NEW_LINE +
                "</notes>"), is("cvc-complex-type.2.4.a: Invalid content was found starting with element 'heading'. One of '{from}' is expected."));
    }

    @Test
    public void shouldHandleXmlExtraField() {
        assertThat(new XmlSchemaValidator(XML_SCHEMA).isValid( "<?xml version=\"1.0\" encoding=\"utf-8\"?>" + NEW_LINE +
                        "<notes>" + NEW_LINE +
                        "    <note>" + NEW_LINE +
                        "        <to>Bob</to>" + NEW_LINE +
                        "        <to>Bob</to>" + NEW_LINE +
                        "        <from>Bill</from>" + NEW_LINE +
                        "        <heading>Reminder</heading>" + NEW_LINE +
                        "        <body>Buy Bread</body>" + NEW_LINE +
                        "    </note>" + NEW_LINE +
                        "    <note>" + NEW_LINE +
                        "        <to>Jack</to>" + NEW_LINE +
                        "        <from>Jill</from>" + NEW_LINE +
                        "        <heading>Reminder</heading>" + NEW_LINE +
                        "        <body>Wash Shirts</body>" + NEW_LINE +
                        "    </note>" + NEW_LINE +
                        "</notes>"),
                is("cvc-complex-type.2.4.a: Invalid content was found starting with element 'to'. One of '{from}' is expected."));
    }

    @Test
    public void shouldHandleIllegalXml() {
        // then
        exception.expect(IllegalArgumentException.class);
        exception.expectMessage("Schema must either be a path reference to a *.xsd file or an xml string");

        // when
        new XmlSchemaValidator("illegal_xml");
    }

    @Test
    public void shouldHandleNullExpectation() {
        // then
        exception.expect(NullPointerException.class);

        // when
        new XmlSchemaValidator(null);
    }

    @Test
    public void shouldHandleEmptyExpectation() {
        // then
        exception.expect(IllegalArgumentException.class);
        exception.expectMessage("Schema is not valid");

        // when
        new XmlSchemaValidator("");
    }

    @Test
    public void shouldHandleNullTest() {
        // given
        assertThat(new XmlSchemaValidator(XML_SCHEMA).isValid(null), is("NullPointerException - null"));
    }

    @Test
    public void shouldHandleEmptyTest() {
        // given
        assertThat(new XmlSchemaValidator(XML_SCHEMA).isValid( ""), is("Premature end of file."));
    }
}