/* 
 *  Procedure created to guarantee that the balance transfer will happen
 *  completely in one database call and that it will be logged in the database.
 */

CREATE OR REPLACE PROCEDURE transfer_balance(source_id bigint, dest_id bigint, amount double precision)
LANGUAGE SQL
AS $$
    INSERT INTO bank_account_transaction(source_id, dest_id, amount) values(source_id, dest_id, amount);
    UPDATE bank_account ba SET balance = balance - amount WHERE ba.id = source_id;
    UPDATE bank_account ba SET balance = balance + amount WHERE ba.id = dest_id;
$$ ^;