package com.cloudgames.entities;

import javax.persistence.Entity;
import javax.persistence.Table;

import com.cloudgames.entities.interfaces.TransactionTypeInterface;

@Entity
@Table(name="transaction_type")
public class TransactionType extends AbstractDefinitionEntity implements TransactionTypeInterface {

}
