CREATE TABLE WritingGroup (
GroupName VARCHAR(100) NOT NULL,
HeadWriter VARCHAR(50) NOT NULL,
YearFormed INTEGER NOT NULL,
Subject VARCHAR(50) NOT NULL,
CONSTRAINT WritingGroup_pk
PRIMARY KEY (GroupName)
);

CREATE TABLE Publisher (
PublisherName VARCHAR(75) NOT NULL,
PublisherAddress VARCHAR(100) NOT NULL,
PublisherPhone VARCHAR(20),
PublisherEmail VARCHAR(50),
CONSTRAINT Publisher_pk
PRIMARY KEY (PublisherName)
);

CREATE TABLE Book (
GroupName VARCHAR(100) NOT NULL,
BookTitle VARCHAR(100) NOT NULL,
PublisherName VARCHAR(75) NOT NULL,
YearPublished INTEGER NOT NULL,
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


INSERT INTO WritingGroup
(GroupName, HeadWriter, YearFormed, Subject)
VALUES
('The Time Traveling Writers', 'Mark Twain', 2256 , 'Non-Fiction');


INSERT INTO Publisher
(PublisherName, PublisherAddress, PublisherPhone, PublisherEmail)
VALUES
('Twain Publishing', '1234 Rainbow Road, Mario City New Canada', '631-234-6521', 'contact@marktwain.com');

INSERT INTO Book
(GroupName, BookTitle, PublisherName, YearPublished, NumberPages)
VALUES
('The Time Traveling Writers', 'How to Not Destroy the Space Time Continuum', 'Twain Publishing', 2154, 12);

INSERT INTO Book
(GroupName, BookTitle, PublisherName, YearPublished, NumberPages)
VALUES
('The Time Traveling Writers', 'Mark Twain on the 2016 Election', 'Twain Publishing', 2017, 423);

INSERT INTO Book
(GroupName, BookTitle, PublisherName, YearPublished, NumberPages)
VALUES
('The Time Traveling Writers', 'How to Recreate "Back to the Future" Using a Time Machine', 'Twain Publishing', 1985, 200);

INSERT INTO Book
(GroupName, BookTitle, PublisherName, YearPublished, NumberPages)
VALUES
('The Time Traveling Writers', 'How The New World Order Compares to Orwellian 1984', 'Twain Publishing', 2220, 163);





INSERT INTO WritingGroup
(GroupName, HeadWriter, YearFormed, Subject)
VALUES
('CECS 323', 'Dave Brown', 2014, 'Non-Fiction');

INSERT INTO Publisher
(PublisherName, PublisherAddress, PublisherPhone, PublisherEmail)
VALUES
('Do and Co.', '123 Canada Avenue Cooltown, New Mexico 12345', '949-123-4567', 'kenny@doand.co');

INSERT INTO Book
(GroupName, BookTitle, PublisherName, YearPublished, NumberPages)
VALUES
('CECS 323', 'How to Build a Database', 'Do and Co.', 2017 , 213);

INSERT INTO Book
(GroupName, BookTitle, PublisherName, YearPublished, NumberPages)
VALUES
('CECS 323', 'The Ethical Dilemma That Is Many to Many', 'Do and Co.', 2017 , 143);

INSERT INTO Publisher
(PublisherName, PublisherAddress, PublisherPhone, PublisherEmail)
VALUES
('The Slates Mates Publishing', '1600 Pennsylvania Ave NW, Washington, DC 20500', '714-123-4567', 'wesley@slatespublishing.com');

INSERT INTO Book
(GroupName, BookTitle, PublisherName, YearPublished, NumberPages)
VALUES
('CECS 323', 'Why Wesley and Kenny should get an A. An introspective look.', 'The Slates Mates Publishing', 2017 , 503);

INSERT INTO Book
(GroupName, BookTitle, PublisherName, YearPublished, NumberPages)
VALUES
('CECS 323', 'UML and Relation Schemes: A Love Story', 'The Slates Mates Publishing', 2017 , 278);

SELECT * from Publisher;

Select * from Book;

Select * from WritingGroup;

