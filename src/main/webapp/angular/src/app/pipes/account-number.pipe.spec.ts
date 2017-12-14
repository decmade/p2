import { AccountNumberPipe } from './account-number.pipe';

describe('AccountNumberPipe', () => {
  it('create an instance', () => {
    const pipe = new AccountNumberPipe();
    expect(pipe).toBeTruthy();
  });
});
