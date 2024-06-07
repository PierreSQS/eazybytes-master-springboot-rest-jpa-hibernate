
    alter table person 
       drop 
       foreign key FKk7rgn6djxsv2j2bv1mvuxd4m9;

    alter table person 
       drop 
       foreign key FKlwljuqdohcnacf80bhexwtf3j;

    alter table person 
       drop 
       foreign key FKnpr2oekfnt93l20ykrj3g2n24;

    alter table person_course 
       drop 
       foreign key FKtkcdrhq0ix1n8wuste0659ww3;

    alter table person_course 
       drop 
       foreign key FKia2edykxmqfxypindkr3888j3;

    drop table if exists address;

    drop table if exists class;

    drop table if exists contact_msg;

    drop table if exists course;

    drop table if exists holidays;

    drop table if exists person;

    drop table if exists person_course;

    drop table if exists roles;

    create table address (
        address_id integer not null auto_increment,
        created_at datetime(6),
        updated_at datetime(6),
        address1 varchar(255),
        address2 varchar(255),
        city varchar(255),
        created_by varchar(255),
        state varchar(255),
        updated_by varchar(255),
        zip_code varchar(255),
        primary key (address_id)
    ) engine=InnoDB;

    create table class (
        class_id integer not null auto_increment,
        created_at datetime(6),
        updated_at datetime(6),
        created_by varchar(255),
        name varchar(255),
        updated_by varchar(255),
        primary key (class_id)
    ) engine=InnoDB;

    create table contact_msg (
        contact_id integer not null auto_increment,
        created_at datetime(6),
        updated_at datetime(6),
        created_by varchar(255),
        email varchar(255),
        message varchar(255),
        mobile_num varchar(255),
        name varchar(255),
        status varchar(255),
        subject varchar(255),
        updated_by varchar(255),
        primary key (contact_id)
    ) engine=InnoDB;

    create table course (
        course_id integer not null auto_increment,
        created_at datetime(6),
        updated_at datetime(6),
        created_by varchar(255),
        fees varchar(255),
        name varchar(255),
        updated_by varchar(255),
        primary key (course_id)
    ) engine=InnoDB;

    create table holidays (
        created_at datetime(6),
        updated_at datetime(6),
        created_by varchar(255),
        day varchar(255) not null,
        reason varchar(255),
        updated_by varchar(255),
        type enum ('FESTIVAL','FEDERAL'),
        primary key (day)
    ) engine=InnoDB;

    create table person (
        address_id integer,
        class_id integer,
        person_id integer not null auto_increment,
        role_id integer not null,
        created_at datetime(6),
        updated_at datetime(6),
        created_by varchar(255),
        email varchar(255),
        mobile_number varchar(255),
        name varchar(255),
        pwd varchar(255),
        updated_by varchar(255),
        primary key (person_id)
    ) engine=InnoDB;

    create table person_course (
        course_id integer not null,
        person_id integer not null,
        primary key (course_id, person_id)
    ) engine=InnoDB;

    create table roles (
        role_id integer not null auto_increment,
        created_at datetime(6),
        updated_at datetime(6),
        created_by varchar(255),
        role_name varchar(255),
        updated_by varchar(255),
        primary key (role_id)
    ) engine=InnoDB;

    alter table person 
       add constraint UK_o8tnkglv9n1eeqmo7de7em37n unique (address_id);

    alter table person 
       add constraint UK_blkytayhlgyjt5xbhj0do1i4k unique (role_id);

    alter table person 
       add constraint FKk7rgn6djxsv2j2bv1mvuxd4m9 
       foreign key (address_id) 
       references address (address_id);

    alter table person 
       add constraint FKlwljuqdohcnacf80bhexwtf3j 
       foreign key (class_id) 
       references class (class_id);

    alter table person 
       add constraint FKnpr2oekfnt93l20ykrj3g2n24 
       foreign key (role_id) 
       references roles (role_id);

    alter table person_course 
       add constraint FKtkcdrhq0ix1n8wuste0659ww3 
       foreign key (course_id) 
       references course (course_id);

    alter table person_course 
       add constraint FKia2edykxmqfxypindkr3888j3 
       foreign key (person_id) 
       references person (person_id);
