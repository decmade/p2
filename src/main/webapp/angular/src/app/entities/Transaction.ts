import { TransactionType } from './TransactionType';

export class Transaction {
    id: number;
    amount: number;
    created: Date;
    type: TransactionType;
  }
