DROP TABLE IF EXISTS bbs_reply;
DROP TABLE IF EXISTS bbs_post;
DROP TABLE IF EXISTS bbs_forum;
DROP TABLE IF EXISTS bbs_user;
DROP TABLE IF EXISTS bbs_usergroup;
DROP TABLE IF EXISTS bbs_sensitiveword;

-- 论坛用户表
CREATE TABLE bbs_user (
  userid INTEGER UNSIGNED NOT NULL AUTO_INCREMENT,  -- 自增的唯一的用户id
  username CHAR(20) NOT NULL DEFAULT '',            -- 唯一的用户名，用于登录
  email CHAR(40) NOT NULL DEFAULT '',
  `password` CHAR(42) NOT NULL DEFAULT '',          -- 用户密码（需要加密）
  regdate DATE NOT NULL DEFAULT '1970-1-1',
  usergroupid INTEGER UNSIGNED NOT NULL,
  PRIMARY KEY (userid),
  UNIQUE KEY username (username)
  -- CONSTRAINT user_usergroup_fk FOREIGN KEY (usergroupid) REFERENCES bbs_usergroup (usergroupid)
)ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 论坛板块表
CREATE TABLE bbs_forum (
  forumid INTEGER UNSIGNED NOT NULL AUTO_INCREMENT,
  forumname CHAR(30) NOT NULL DEFAULT '',
  PRIMARY KEY (forumid),
  UNIQUE KEY (forumname)
)ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 帖子表
CREATE TABLE bbs_post (
  postid INTEGER UNSIGNED NOT NULL AUTO_INCREMENT,
  forumid INTEGER UNSIGNED NOT NULL,
  userid INTEGER UNSIGNED NOT NULL,
  title VARCHAR(190) NOT NULL,
  edittime DATETIME NOT NULL DEFAULT '1970-1-1 12:00:00',
  content MEDIUMTEXT NOT NULL,
  top TINYINT(1) UNSIGNED NOT NULL DEFAULT '0',       -- 是否置顶

  PRIMARY KEY (postid),
  KEY forumid (forumid),
  KEY userid (userid),
  KEY title (title),
  KEY edittime (edittime),
  KEY top (top)
  -- CONSTRAINT post_forum_fk FOREIGN KEY (forumid) REFERENCES bbs_forum (forumid),
  -- CONSTRAINT post_user_fk FOREIGN KEY (userid) REFERENCES bbs_user (userid)
)ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 回复表
CREATE TABLE bbs_reply (
  replyid INTEGER UNSIGNED NOT NULL AUTO_INCREMENT,
  postid INTEGER UNSIGNED NOT NULL,
  userid INTEGER UNSIGNED NOT NULL,
  edittime DATETIME NOT NULL DEFAULT '1970-1-1 12:00:00',
  content MEDIUMTEXT NOT NULL,

  PRIMARY KEY (replyid),
  KEY postid (postid),
  KEY userid (userid)
  -- CONSTRAINT reply_post_fk FOREIGN KEY (postid) REFERENCES bbs_post (postid),
  -- CONSTRAINT reply_user_fk FOREIGN KEY (userid) REFERENCES bbs_user (userid)
)ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 用户组表
CREATE TABLE bbs_usergroup (
  usergroupid INTEGER UNSIGNED NOT NULL AUTO_INCREMENT, -- 用户组id
  usergroupname CHAR(20) NOT NULL DEFAULT '',

  allowpost TINYINT(1) NOT NULL DEFAULT '0',            -- 允许发帖（1为允许，下同）
  allowreply TINYINT(1) NOT NULL DEFAULT '0',           -- 允许回复

  alloweditpost TINYINT(1) NOT NULL DEFAULT '0',        -- 允许编辑帖子
  allowtoppost TINYINT(1) NOT NULL DEFAULT '0',         -- 允许置顶帖子
  allowdelpost TINYINT(1) NOT NULL DEFAULT '0',         -- 允许删帖
  allowedituser TINYINT(1) NOT NULL DEFAULT '0',        -- 允许编辑用户
  allowbanuser TINYINT(1) NOT NULL DEFAULT '0',         -- 允许禁言用户
  alloweditforum TINYINT(1) NOT NULL DEFAULT '0',       -- 允许编辑板块
  PRIMARY KEY (usergroupid),
  UNIQUE KEY (usergroupname)
)ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE bbs_sensitiveword (
  wid INTEGER(11) NOT NULL AUTO_INCREMENT,
  word VARCHAR(256) NOT NULL,
  PRIMARY KEY (wid)
)ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;