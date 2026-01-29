package ec.com.tecnointel.soem.serWebClientSri.general;

import jakarta.xml.bind.annotation.adapters.XmlAdapter;

public class CDATAAdapter extends XmlAdapter<String, String> {
    @Override
    public String unmarshal(String v) {
        return v;
    }

    @Override
    public String marshal(String v) {
        return "<![CDATA[" + v + "]]>";
    }
}
