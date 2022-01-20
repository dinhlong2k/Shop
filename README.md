* Xoá 1 bảng có foreign key trong MysQ

VD xoá bảng người dùng " set foreign_key_checks=0;
truncate table users";

* Sử dụng 1 Schema trong database: "USE {Schema}"
* Lecture  59:

  1. Role

     + Admin has access: Users,Categories,Brands,Products,Question, Reviews,
       Customers,Shipping,Orders, Reports,Articles, Menus, Setting
     + SalesPerson has access: Products,Customers,Shipping,Orders,Reports
     + Editor has access: Categories, Brands,Products, Articles, Menus
     + Shippers has access: Products,Orders
     + Assistant has access: Question, Reviews
* Lecture 71:

  + List Categories Hierarchi and sub Hierarchi leen bảng
* Lecture 72: Update Category

  + Update Categories
  + Hiển thị lỗi nếu category Id không tìm thấy hoặc không có
  + Trong image edit, image không yêu cầu
  + Nếu không có ảnh được upload, không chỉnh sửa ảnh hiện tại
* Lecture 74: Check unique

  + Sử dụng rest api check name va alias neu trung thi tra ve false. Ngược lại trả về true
  + Trả về trong hàm checkIdNameAlias() trong lớp CategoryServiceImp. Hàm trả về chuỗi String
    "OK", "DuplicateName","DuplicateAlias"
* Lecture 75: Code Sort

  + Người dùng có thể sort với tên category theo tăng dần hoặc giảm dần
  + Sort với root category, sau đó tới sub category
  + Truyền vào giá trị sortDir: nếu là "asc" thì sắp xếp cho cả root category và sub category

*Lecture 76: Update Enabled Status

*Lecture 77: Code delete Category function

+ Thêm thuộc tính hasParent với annotation @Transient trong class Category
+ Trong function copyFullProperties trong class Category. Set giá trị hasChildren là true hoặc false dựa vào size của getChildren
+ Update html category:
  + Thêm th:block kiểm tra nếu hasChildren= false (nếu category đó không có sub category) thì hiển thị ra button delete
  + Ngược lại nếu hasChildren = true (nếu category đó có sub category) thì không hiển thị ra button delete

*Lecture 78: Code Pagination for Category List

+ Phân trang dựa theo root Category (chỉ liệt kê ra những category cha chứ không phân trang theo sub category)
+ Show 4 root - category mỗi trang

*Lecture 85: Understand Requirement of Brand Module

  + Name of brand or company: must be unique
  + Logo: image of brand
  + Categories: Một product đưỢc làm bởi brand hoặc company có thể bao gồm nhiều categories

  + Requirement: Admin & Editor 
    - Manage Brands: List Brands, Create new brand, Update Existing brand, Export brand to CSV, Delete Brand

* Lecture 86: Understand Technical Design for Brand Module

  + Brands & Category có mối quan hệ N-N: Một brands thì có nhiều categories và một categories cũng có thể có nhiều brand 
  => * Xoá 1 bảng có foreign key trong MysQ

VD xoá bảng người dùng " set foreign_key_checks=0;
truncate table users";

* Sử dụng 1 Schema trong database: "USE {Schema}"
* Lecture  59:

  1. Role

     + Admin has access: Users,Categories,Brands,Products,Question, Reviews,
       Customers,Shipping,Orders, Reports,Articles, Menus, Setting
     + SalesPerson has access: Products,Customers,Shipping,Orders,Reports
     + Editor has access: Categories, Brands,Products, Articles, Menus
     + Shippers has access: Products,Orders
     + Assistant has access: Question, Reviews
* Lecture 71:

  + List Categories Hierarchi and sub Hierarchi leen bảng
* Lecture 72: Update Category

  + Update Categories
  + Hiển thị lỗi nếu category Id không tìm thấy hoặc không có
  + Trong image edit, image không yêu cầu
  + Nếu không có ảnh được upload, không chỉnh sửa ảnh hiện tại
* Lecture 74: Check unique

  + Sử dụng rest api check name va alias neu trung thi tra ve false. Ngược lại trả về true
  + Trả về trong hàm checkIdNameAlias() trong lớp CategoryServiceImp. Hàm trả về chuỗi String
    "OK", "DuplicateName","DuplicateAlias"
* Lecture 75: Code Sort

  + Người dùng có thể sort với tên category theo tăng dần hoặc giảm dần
  + Sort với root category, sau đó tới sub category
  + Truyền vào giá trị sortDir: nếu là "asc" thì sắp xếp cho cả root category và sub category

*Lecture 76: Update Enabled Status

*Lecture 77: Code delete Category function

+ Thêm thuộc tính hasParent với annotation @Transient trong class Category
+ Trong function copyFullProperties trong class Category. Set giá trị hasChildren là true hoặc false dựa vào size của getChildren
+ Update html category:
  + Thêm th:block kiểm tra nếu hasChildren= false (nếu category đó không có sub category) thì hiển thị ra button delete
  + Ngược lại nếu hasChildren = true (nếu category đó có sub category) thì không hiển thị ra button delete

*Lecture 78: Code Pagination for Category List

+ Phân trang dựa theo root Category (chỉ liệt kê ra những category cha chứ không phân trang theo sub category)
+ Show 4 root - category mỗi trang

*Lecture 85: Understand Requirement of Brand Module

  + Name of brand or company: must be unique
  + Logo: image of brand
  + Categories: Một product đưỢc làm bởi brand hoặc company có thể bao gồm nhiều categories

  + Requirement: Admin & Editor 
    - Manage Brands: List Brands, Create new brand, Update Existing brand, Export brand to CSV, Delete Brand

* Lecture 86: Understand Technical Design for Brand Module

  + Brands & Category có mối quan hệ N-N: Một brands thì có nhiều categories và một categories cũng có thể có nhiều brand 
  => 
