package com.example.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Grade {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(name = "grade_name")
    private String gradeName;

    //mappedBy 维护外键关联关系    由学生实体类中的实体类grade维护
//    @OneToOne(mappedBy = "grade")
//    private Student student;


    //模拟一对多      FetchType.EAGER 立即加载
    @OneToMany(mappedBy = "grade",cascade = CascadeType.ALL,fetch = FetchType.EAGER)
    //排序  按照某个属性或字段排序
    @OrderBy("name asc ")
    private List<Student> student = new ArrayList<>();

    public List<Student> getStudent() {
        return student;
    }

    public void setStudent(List<Student> student) {
        this.student = student;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getGradeName() {
        return gradeName;
    }

    public void setGradeName(String gradeName) {
        this.gradeName = gradeName;
    }

//    public Student getStudent() {
//        return student;
//    }
//
//    public void setStudent(Student student) {
//        this.student = student;
//    }

    @Override
    public String toString() {
        return "Grade{" +
                "id=" + id +
                ", gradeName='" + gradeName + '\'' +
                '}';
    }
}
