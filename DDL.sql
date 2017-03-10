CREATE TABLE WritingGroup (
GroupName VARCHAR(20) NOT NULL,
HeadWriter VARCHAR(20) NOT NULL,
YearFormed DATE NOT NULL,
Subject VARCHAR(20) NOT NULL,
CONSTRAINT WritingGroup_pk
PRIMARY KEY (GroupName)
);

CREATE TABLE Publisher (
PublisherName VARCHAR(20) NOT NULL,
PublisherAddress VARCHAR(20) NOT NULL,
PublisherPhone VARCHAR(20),
PublisherEmail VARCHAR(50),
CONSTRAINT Publisher_pk
PRIMARY KEY (PublisherName)
);

CREATE TABLE Book (
GroupName VARCHAR(20) NOT NULL,
BookTitle VARCHAR(50) NOT NULL,
PublisherName VARCHAR(20) NOT NULL,
YearPublished DATE NOT NULL,
NumberPages INTEGER NOT NULL,
CONSTRAINT Book_fk01 
FOREIGN KEY (GroupName)
REFERENCES WritingGroup(GroupName),
CONSTRAINT Book_fk02
FOREIGN KEY (PublisherName)
REFERENCES Publisher(PublisherName),
CONSTRAINT Book_uk01
UNIQUE (BookTitle, PublisherName),
CONSTRAINT Book_pk 
PRIMARY KEY (GroupName, BookTitle)
);

DROP TABLE Publisher;
DROP TABLE Book;

INSERT INTO WritingGroup
(GroupName, HeadWriter, YearFormed, Subject)
VALUES
('Coffee House', 'Spongebob', '2013-5-20', 'Fiction');

INSERT INTO Publisher
(PublisherName, PublisherAddress, PublisherPhone, PublisherEmail)
VALUES
('Scholastic', '1234 Rainbow Road', '631-234-6521', 'contact@scholastic.com');

INSERT INTO Book
(GroupName, BookTitle, PublisherName, YearPublished, NumberPages)
VALUES
('Coffee House', 'The Adventure of Spongebob', 'Scholastic', '2014-2-11', 43);

SELECT PublisherName, PublisherAddress, PublisherPhone, PublisherEmail FROM Publisher;