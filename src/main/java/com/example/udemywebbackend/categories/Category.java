package com.example.udemywebbackend.categories;

import com.example.udemywebbackend.admin.Upload.AWS.Contants;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "category")
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(length = 128, nullable = false,unique = true)
    private String name;

    //name of url display in client
    @Column(length = 128, nullable = false,unique = true)
    private String alias;

    @Column(length = 256, nullable = false)
    private String image;

    private boolean enabled;

    @Transient
	private boolean hasChildren;

    //refer to parent category, moi mot category se co mot category parent
    @OneToOne
    @JoinColumn(name = "parent_id")
    private Category parent;

    //refering to set up subcategories as the category
    @OneToMany(mappedBy = "parent")
    private Set<Category> childern=new HashSet<>();

    public Category() {
    }

    public Category(Integer id) {
        this.id = id;
    }
    
    public static Category copyIdAndNameCate(Integer id, String name){
        Category category=new Category();
        category.setId(id);
        category.setName(name);

        return category;
    }

    public Category(String name, String alias, String image, boolean enabled) {
        this.name = name;
        this.alias = alias;
        this.image = image;
        this.enabled = enabled;
    }

    public static Category copyFullPropertiesCategory(Category category,String name){
        Category categoryCopy=new Category();
        categoryCopy.setName(name);
        categoryCopy.setImage(category.getImage());
        categoryCopy.setAlias(category.getAlias());
        categoryCopy.setId(category.getId());
        categoryCopy.setEnabled(category.isEnabled());
        categoryCopy.setHasChildren(category.getChildern().size() >0); //kiem tra neu co child thi khong show button delete va nguoc lai 
        return categoryCopy;
    }

    public Integer getId() {
        return this.id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
    
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public Category getParent() {
        return parent;
    }

    public void setParent(Category parent) {
        this.parent = parent;
    }

    public Set<Category> getChildern() {
        return childern;
    }

    public void setChildern(Set<Category> childern) {
        this.childern = childern;
    }

    @Override
    public String toString() {
        return "Category{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", alias='" + alias + '\'' +
                ", image='" + image + '\'' +
                '}';
    }

    @Transient
    public String getPhotoImagePath(){
        if(image==null) return "/images/thumbCate.png";

        else{
            return Contants.S3_BASE_URI + "categories-photos/" + this.id + "/" +this.image;
        }
    }

    public boolean isHasChildren() {
		return hasChildren;
	}

	public void setHasChildren(boolean hasChildren) {
		this.hasChildren = hasChildren;
	}

}
