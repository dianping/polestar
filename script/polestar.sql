CREATE DATABASE `polestar` /*!40100 DEFAULT CHARACTER SET utf8 COLLATE utf8_unicode_ci */

CREATE TABLE `QueryInfo` (
  `id` bigint(11) NOT NULL AUTO_INCREMENT,
  `username` varchar(50) COLLATE utf8_unicode_ci NOT NULL,
  `sql` mediumtext COLLATE utf8_unicode_ci NOT NULL,
  `mode` varchar(10) COLLATE utf8_unicode_ci NOT NULL,
  `addtime` datetime NOT NULL,
  `exectime` bigint(11) NOT NULL,
  `path` varchar(500) COLLATE utf8_unicode_ci DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci