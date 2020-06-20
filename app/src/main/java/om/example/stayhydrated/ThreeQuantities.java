package om.example.stayhydrated;

public class ThreeQuantities {
    int id;
    String _quantity;
    String TimeStamp;
    String RegularDate;

    public String getRegularDate() {
        return RegularDate;
    }

    public void setRegularDate(String regularDate) {
        RegularDate = regularDate;
    }



    public String getTimeStamp() {
        return TimeStamp;
    }

    public void setTimeStamp(String timeStamp) {
        TimeStamp = timeStamp;
    }

    public ThreeQuantities() {
    }


    public ThreeQuantities(int id, String _quantity, String timeStamp,String regularDate) {
        this.id = id;
        this._quantity = _quantity;
        TimeStamp = timeStamp;
        RegularDate=regularDate;
    }



    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String get_quantity() {
        return _quantity;
    }

    public void set_quantity(String _quantity) {
        this._quantity = _quantity;
    }

    int getImageId(){
        switch (this._quantity) {
            case "50":
                return R.drawable.icons8_water_droplet;
            case "200":
                return R.drawable.icons8_water_glass;
            case "350":
                return R.drawable.icons8_water_bottle;
            default:
                throw new IllegalArgumentException();
        }
    }
}
