package com.cloudgames.entities;

import javax.persistence.*;

import com.cloudgames.entities.interfaces.ScoreInterface;


@Entity
@Table(name = "sports")
public class Sport extends AbstractDefinitionEntity implements ScoreInterface {

}
