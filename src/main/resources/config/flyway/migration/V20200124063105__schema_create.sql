drop table if exists Persons
CREATE TABLE Persons(
  code int not null identity(1,1),
  name varchar(50) null,
  surname varchar(50) null,
  id_number varchar(50) not null,

  constraint PK_Persons primary key clustered
  (
    code
  )
)
GO

CREATE TABLE Accounts(
  code int not null identity(1,1),
  person_code int not null,
  account_number varchar(50) not null,
  outstanding_balance money not null,

  constraint FK_Account_Person foreign key (person_code) references Persons(code),

  constraint PK_Accounts primary key clustered
  (
    code
  )
)
GO
CREATE TABLE Transactions(
  code int not null identity(1,1),
  account_code int not null,
  transaction_date datetime not null,
  capture_date datetime not null,
  amount money not null,
  description varchar(100) not null,

  constraint FK_Transaction_Account foreign key (account_code) references Accounts(code),

  constraint PK_Transactions primary key clustered
  (
    code
  )
)
GO
CREATE UNIQUE NONCLUSTERED INDEX IX_Person_id ON Persons(id_number)
GO
CREATE UNIQUE NONCLUSTERED INDEX IX_Account_num ON Accounts(account_number)
GO