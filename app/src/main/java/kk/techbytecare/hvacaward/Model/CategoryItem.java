package kk.techbytecare.hvacaward.Model;

public class CategoryItem {

    private String Name;
    private String Image;

    public CategoryItem() {
    }

    public CategoryItem(String name, String image) {
        Name = name;
        Image = image;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getImage() {
        return Image;
    }

    public void setImage(String image) {
        Image = image;
    }
}
