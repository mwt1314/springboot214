package morphia;

import org.bson.types.ObjectId;
import org.mongodb.morphia.annotations.*;

import java.util.List;

@Entity()
public class Commodity {

    @Id
    private ObjectId id;

    @Indexed
    private String cmdtyCode;

    private String cmdtyName;

    @Embedded
    private List<Attribute> attributes;

    @Reference
    private CommodityInfo commodityInfo;

    public ObjectId getId() {
        return id;
    }

    public void setId(ObjectId id) {
        this.id = id;
    }

    public String getCmdtyCode() {
        return cmdtyCode;
    }

    public void setCmdtyCode(String cmdtyCode) {
        this.cmdtyCode = cmdtyCode;
    }

    public String getCmdtyName() {
        return cmdtyName;
    }

    public void setCmdtyName(String cmdtyName) {
        this.cmdtyName = cmdtyName;
    }

    public List<Attribute> getAttributes() {
        return attributes;
    }

    public void setAttributes(List<Attribute> attributes) {
        this.attributes = attributes;
    }

    public CommodityInfo getCommodityInfo() {
        return commodityInfo;
    }

    public void setCommodityInfo(CommodityInfo commodityInfo) {
        this.commodityInfo = commodityInfo;
    }
}