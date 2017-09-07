/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kles.model;

import javafx.scene.image.Image;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Jeremy.CHAUT
 */
@XmlRootElement(name="Language")
public class Language {

    private final String code;
    private final Image icon;

    public Language(String name) {
        this.code = name;
        if (ClassLoader.getSystemResourceAsStream("resources/images/lng_" + name + ".png") != null) {
            this.icon = new Image(ClassLoader.getSystemResourceAsStream("resources/images/lng_" + name + ".png"));
        } else {
            this.icon = new Image(ClassLoader.getSystemResourceAsStream("resources/images/3KLES_16.png"));
        }
    }

    public Image getIcon() {
        return icon;
    }

    public String getCode() {
        return code;
    }

    @Override
    public String toString() {
        return code;
    }
}
