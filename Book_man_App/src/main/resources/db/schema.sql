CREATE DATABASE IF NOT EXISTS bookappdb;
USE bookappdb;

CREATE TABLE author (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    birth_date DATE
);

CREATE TABLE book (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    isbn VARCHAR(13) UNIQUE NOT NULL,
    publication_date DATE,
    price DECIMAL(10,2),
    author_id BIGINT,
    FOREIGN KEY (author_id) REFERENCES author(id),
    INDEX idx_book_isbn (isbn)
);

DELIMITER //
CREATE PROCEDURE GetBooksByAuthor(IN authorId BIGINT)
BEGIN
    SELECT * FROM book WHERE author_id = authorId;
END //
DELIMITER ;

DELIMITER //
CREATE FUNCTION CalculateDiscountedPrice(bookId BIGINT, discount DECIMAL(5,2))
RETURNS DECIMAL(10,2)
DETERMINISTIC
BEGIN
    DECLARE originalPrice DECIMAL(10,2);
    SELECT price INTO originalPrice FROM book WHERE id = bookId;
    RETURN originalPrice * (1 - discount/100);
END //
DELIMITER ;

DELIMITER //
CREATE TRIGGER before_book_insert
BEFORE INSERT ON book
FOR EACH ROW
BEGIN
    IF NEW.price < 0 THEN
        SIGNAL SQLSTATE '45000'
        SET MESSAGE_TEXT = 'Price cannot be negative';
    END IF;
END //
DELIMITER ;

DELIMITER //
CREATE PROCEDURE UpdateBookPrices()
BEGIN
    DECLARE done INT DEFAULT FALSE;
    DECLARE b_id BIGINT;
    DECLARE b_price DECIMAL(10,2);
    DECLARE book_cursor CURSOR FOR SELECT id, price FROM book;
    DECLARE CONTINUE HANDLER FOR NOT FOUND SET done = TRUE;

    OPEN book_cursor;
    read_loop: LOOP
        FETCH book_cursor INTO b_id, b_price;
        IF done THEN
            LEAVE read_loop;
        END IF;
        UPDATE book SET price = b_price * 1.1 WHERE id = b_id;
    END LOOP;
    CLOSE book_cursor;
END //
DELIMITER ;