-- ============================================
-- Flyway Migration: V001__Initial_Schema.sql
-- ============================================
-- Description: Create initial database schema
-- Date: 2025-12-09 17:29:30
-- Version: 1.0.0
-- ============================================

-- Set charset for database compatibility
SET NAMES utf8mb4;
SET CHARACTER SET utf8mb4;
SET COLLATION_CONNECTION = utf8mb4_unicode_ci;

-- ============================================
-- Table: utilisateur (Utilisateur)
-- ============================================
CREATE TABLE IF NOT EXISTS utilisateur (
    id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY COMMENT 'Primary Key',
    Integer PK VARCHAR(255) COMMENT 'Integer PK field',
    String VARCHAR(255) COMMENT 'String field',
    String VARCHAR(255) COMMENT 'String field',
    DateTime VARCHAR(255) COMMENT 'DateTime field',
    DateTime VARCHAR(255) COMMENT 'DateTime field',
    status VARCHAR(50) NOT NULL DEFAULT 'ACTIVE' COMMENT 'Entity status (ACTIVE, INACTIVE, SUSPENDED)',
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT 'Record creation timestamp',
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT 'Last update timestamp',
    version BIGINT NOT NULL DEFAULT 0 COMMENT 'Optimistic locking version'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='Utilisateur entity table';

CREATE INDEX IF NOT EXISTS idx_utilisateur_created_at ON utilisateur (created_at) COMMENT 'Index for time-based queries';
CREATE INDEX IF NOT EXISTS idx_utilisateur_status ON utilisateur (status) COMMENT 'Index for status filtering';

-- ============================================
-- Table: administrateur (Administrateur)
-- ============================================
CREATE TABLE IF NOT EXISTS administrateur (
    id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY COMMENT 'Primary Key',
    Boolean VARCHAR(255) COMMENT 'Boolean field',
    Integer VARCHAR(255) COMMENT 'Integer field',
    status VARCHAR(50) NOT NULL DEFAULT 'ACTIVE' COMMENT 'Entity status (ACTIVE, INACTIVE, SUSPENDED)',
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT 'Record creation timestamp',
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT 'Last update timestamp',
    version BIGINT NOT NULL DEFAULT 0 COMMENT 'Optimistic locking version'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='Administrateur entity table';

CREATE INDEX IF NOT EXISTS idx_administrateur_created_at ON administrateur (created_at) COMMENT 'Index for time-based queries';
CREATE INDEX IF NOT EXISTS idx_administrateur_status ON administrateur (status) COMMENT 'Index for status filtering';

-- ============================================
-- Table: article (Article)
-- ============================================
CREATE TABLE IF NOT EXISTS article (
    id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY COMMENT 'Primary Key',
    Integer PK VARCHAR(255) COMMENT 'Integer PK field',
    String VARCHAR(255) COMMENT 'String field',
    Text VARCHAR(255) COMMENT 'Text field',
    DateTime VARCHAR(255) COMMENT 'DateTime field',
    DateTime VARCHAR(255) COMMENT 'DateTime field',
    String VARCHAR(255) COMMENT 'String field',
    String VARCHAR(255) COMMENT 'String field',
    Integer FK VARCHAR(255) COMMENT 'Integer FK field',
    Integer FK VARCHAR(255) COMMENT 'Integer FK field',
    String VARCHAR(255) COMMENT 'String field',
    String VARCHAR(255) COMMENT 'String field',
    status VARCHAR(50) NOT NULL DEFAULT 'ACTIVE' COMMENT 'Entity status (ACTIVE, INACTIVE, SUSPENDED)',
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT 'Record creation timestamp',
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT 'Last update timestamp',
    version BIGINT NOT NULL DEFAULT 0 COMMENT 'Optimistic locking version'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='Article entity table';

CREATE INDEX IF NOT EXISTS idx_article_created_at ON article (created_at) COMMENT 'Index for time-based queries';
CREATE INDEX IF NOT EXISTS idx_article_status ON article (status) COMMENT 'Index for status filtering';

-- ============================================
-- Table: bloc_contenu (BlocContenu)
-- ============================================
CREATE TABLE IF NOT EXISTS bloc_contenu (
    id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY COMMENT 'Primary Key',
    Integer PK VARCHAR(255) COMMENT 'Integer PK field',
    Integer FK VARCHAR(255) COMMENT 'Integer FK field',
    JSON VARCHAR(255) COMMENT 'JSON field',
    Integer VARCHAR(255) COMMENT 'Integer field',
    Integer VARCHAR(255) COMMENT 'Integer field',
    Integer FK VARCHAR(255) COMMENT 'Integer FK field',
    JSON VARCHAR(255) COMMENT 'JSON field',
    Integer FK VARCHAR(255) COMMENT 'Integer FK field',
    status VARCHAR(50) NOT NULL DEFAULT 'ACTIVE' COMMENT 'Entity status (ACTIVE, INACTIVE, SUSPENDED)',
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT 'Record creation timestamp',
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT 'Last update timestamp',
    version BIGINT NOT NULL DEFAULT 0 COMMENT 'Optimistic locking version'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='BlocContenu entity table';

CREATE INDEX IF NOT EXISTS idx_bloc_contenu_created_at ON bloc_contenu (created_at) COMMENT 'Index for time-based queries';
CREATE INDEX IF NOT EXISTS idx_bloc_contenu_status ON bloc_contenu (status) COMMENT 'Index for status filtering';

-- ============================================
-- Table: rubrique (Rubrique)
-- ============================================
CREATE TABLE IF NOT EXISTS rubrique (
    id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY COMMENT 'Primary Key',
    Integer PK VARCHAR(255) COMMENT 'Integer PK field',
    String VARCHAR(255) COMMENT 'String field',
    Text VARCHAR(255) COMMENT 'Text field',
    String VARCHAR(255) COMMENT 'String field',
    Integer FK VARCHAR(255) COMMENT 'Integer FK field',
    Integer VARCHAR(255) COMMENT 'Integer field',
    String VARCHAR(255) COMMENT 'String field',
    String VARCHAR(255) COMMENT 'String field',
    Boolean VARCHAR(255) COMMENT 'Boolean field',
    status VARCHAR(50) NOT NULL DEFAULT 'ACTIVE' COMMENT 'Entity status (ACTIVE, INACTIVE, SUSPENDED)',
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT 'Record creation timestamp',
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT 'Last update timestamp',
    version BIGINT NOT NULL DEFAULT 0 COMMENT 'Optimistic locking version'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='Rubrique entity table';

CREATE INDEX IF NOT EXISTS idx_rubrique_created_at ON rubrique (created_at) COMMENT 'Index for time-based queries';
CREATE INDEX IF NOT EXISTS idx_rubrique_status ON rubrique (status) COMMENT 'Index for status filtering';

-- ============================================
-- Table: tag (Tag)
-- ============================================
CREATE TABLE IF NOT EXISTS tag (
    id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY COMMENT 'Primary Key',
    Integer PK VARCHAR(255) COMMENT 'Integer PK field',
    String VARCHAR(255) COMMENT 'String field',
    String VARCHAR(255) COMMENT 'String field',
    String VARCHAR(255) COMMENT 'String field',
    Integer VARCHAR(255) COMMENT 'Integer field',
    status VARCHAR(50) NOT NULL DEFAULT 'ACTIVE' COMMENT 'Entity status (ACTIVE, INACTIVE, SUSPENDED)',
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT 'Record creation timestamp',
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT 'Last update timestamp',
    version BIGINT NOT NULL DEFAULT 0 COMMENT 'Optimistic locking version'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='Tag entity table';

CREATE INDEX IF NOT EXISTS idx_tag_created_at ON tag (created_at) COMMENT 'Index for time-based queries';
CREATE INDEX IF NOT EXISTS idx_tag_status ON tag (status) COMMENT 'Index for status filtering';

-- ============================================
-- Table: media_file (MediaFile)
-- ============================================
CREATE TABLE IF NOT EXISTS media_file (
    id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY COMMENT 'Primary Key',
    Integer PK VARCHAR(255) COMMENT 'Integer PK field',
    String VARCHAR(255) COMMENT 'String field',
    String VARCHAR(255) COMMENT 'String field',
    String VARCHAR(255) COMMENT 'String field',
    Integer VARCHAR(255) COMMENT 'Integer field',
    String VARCHAR(255) COMMENT 'String field',
    DateTime VARCHAR(255) COMMENT 'DateTime field',
    Integer FK VARCHAR(255) COMMENT 'Integer FK field',
    String VARCHAR(255) COMMENT 'String field',
    String VARCHAR(255) COMMENT 'String field',
    String VARCHAR(255) COMMENT 'String field',
    Integer VARCHAR(255) COMMENT 'Integer field',
    status VARCHAR(50) NOT NULL DEFAULT 'ACTIVE' COMMENT 'Entity status (ACTIVE, INACTIVE, SUSPENDED)',
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT 'Record creation timestamp',
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT 'Last update timestamp',
    version BIGINT NOT NULL DEFAULT 0 COMMENT 'Optimistic locking version'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='MediaFile entity table';

CREATE INDEX IF NOT EXISTS idx_media_file_created_at ON media_file (created_at) COMMENT 'Index for time-based queries';
CREATE INDEX IF NOT EXISTS idx_media_file_status ON media_file (status) COMMENT 'Index for status filtering';

-- ============================================
-- Table: media_variant (MediaVariant)
-- ============================================
CREATE TABLE IF NOT EXISTS media_variant (
    id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY COMMENT 'Primary Key',
    Integer PK VARCHAR(255) COMMENT 'Integer PK field',
    Integer FK VARCHAR(255) COMMENT 'Integer FK field',
    Integer VARCHAR(255) COMMENT 'Integer field',
    Integer VARCHAR(255) COMMENT 'Integer field',
    Integer VARCHAR(255) COMMENT 'Integer field',
    String VARCHAR(255) COMMENT 'String field',
    Float VARCHAR(255) COMMENT 'Float field',
    String VARCHAR(255) COMMENT 'String field',
    status VARCHAR(50) NOT NULL DEFAULT 'ACTIVE' COMMENT 'Entity status (ACTIVE, INACTIVE, SUSPENDED)',
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT 'Record creation timestamp',
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT 'Last update timestamp',
    version BIGINT NOT NULL DEFAULT 0 COMMENT 'Optimistic locking version'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='MediaVariant entity table';

CREATE INDEX IF NOT EXISTS idx_media_variant_created_at ON media_variant (created_at) COMMENT 'Index for time-based queries';
CREATE INDEX IF NOT EXISTS idx_media_variant_status ON media_variant (status) COMMENT 'Index for status filtering';

-- ============================================
-- Table: media_usage (MediaUsage)
-- ============================================
CREATE TABLE IF NOT EXISTS media_usage (
    id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY COMMENT 'Primary Key',
    Integer PK VARCHAR(255) COMMENT 'Integer PK field',
    Integer FK VARCHAR(255) COMMENT 'Integer FK field',
    Integer FK VARCHAR(255) COMMENT 'Integer FK field',
    Integer VARCHAR(255) COMMENT 'Integer field',
    DateTime VARCHAR(255) COMMENT 'DateTime field',
    status VARCHAR(50) NOT NULL DEFAULT 'ACTIVE' COMMENT 'Entity status (ACTIVE, INACTIVE, SUSPENDED)',
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT 'Record creation timestamp',
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT 'Last update timestamp',
    version BIGINT NOT NULL DEFAULT 0 COMMENT 'Optimistic locking version'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='MediaUsage entity table';

CREATE INDEX IF NOT EXISTS idx_media_usage_created_at ON media_usage (created_at) COMMENT 'Index for time-based queries';
CREATE INDEX IF NOT EXISTS idx_media_usage_status ON media_usage (status) COMMENT 'Index for status filtering';

-- ============================================
-- Table: media_processing_job (MediaProcessingJob)
-- ============================================
CREATE TABLE IF NOT EXISTS media_processing_job (
    id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY COMMENT 'Primary Key',
    Integer PK VARCHAR(255) COMMENT 'Integer PK field',
    Integer FK VARCHAR(255) COMMENT 'Integer FK field',
    Integer VARCHAR(255) COMMENT 'Integer field',
    String VARCHAR(255) COMMENT 'String field',
    Text VARCHAR(255) COMMENT 'Text field',
    DateTime VARCHAR(255) COMMENT 'DateTime field',
    DateTime VARCHAR(255) COMMENT 'DateTime field',
    status VARCHAR(50) NOT NULL DEFAULT 'ACTIVE' COMMENT 'Entity status (ACTIVE, INACTIVE, SUSPENDED)',
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT 'Record creation timestamp',
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT 'Last update timestamp',
    version BIGINT NOT NULL DEFAULT 0 COMMENT 'Optimistic locking version'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='MediaProcessingJob entity table';

CREATE INDEX IF NOT EXISTS idx_media_processing_job_created_at ON media_processing_job (created_at) COMMENT 'Index for time-based queries';
CREATE INDEX IF NOT EXISTS idx_media_processing_job_status ON media_processing_job (status) COMMENT 'Index for status filtering';

-- ============================================
-- Table: media_license (MediaLicense)
-- ============================================
CREATE TABLE IF NOT EXISTS media_license (
    id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY COMMENT 'Primary Key',
    Integer PK VARCHAR(255) COMMENT 'Integer PK field',
    Integer FK VARCHAR(255) COMMENT 'Integer FK field',
    String VARCHAR(255) COMMENT 'String field',
    String VARCHAR(255) COMMENT 'String field',
    Text VARCHAR(255) COMMENT 'Text field',
    DateTime VARCHAR(255) COMMENT 'DateTime field',
    status VARCHAR(50) NOT NULL DEFAULT 'ACTIVE' COMMENT 'Entity status (ACTIVE, INACTIVE, SUSPENDED)',
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT 'Record creation timestamp',
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT 'Last update timestamp',
    version BIGINT NOT NULL DEFAULT 0 COMMENT 'Optimistic locking version'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='MediaLicense entity table';

CREATE INDEX IF NOT EXISTS idx_media_license_created_at ON media_license (created_at) COMMENT 'Index for time-based queries';
CREATE INDEX IF NOT EXISTS idx_media_license_status ON media_license (status) COMMENT 'Index for status filtering';

-- ============================================
-- Table: video_stream (VideoStream)
-- ============================================
CREATE TABLE IF NOT EXISTS video_stream (
    id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY COMMENT 'Primary Key',
    Integer PK VARCHAR(255) COMMENT 'Integer PK field',
    Integer FK VARCHAR(255) COMMENT 'Integer FK field',
    String VARCHAR(255) COMMENT 'String field',
    Integer VARCHAR(255) COMMENT 'Integer field',
    String VARCHAR(255) COMMENT 'String field',
    status VARCHAR(50) NOT NULL DEFAULT 'ACTIVE' COMMENT 'Entity status (ACTIVE, INACTIVE, SUSPENDED)',
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT 'Record creation timestamp',
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT 'Last update timestamp',
    version BIGINT NOT NULL DEFAULT 0 COMMENT 'Optimistic locking version'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='VideoStream entity table';

CREATE INDEX IF NOT EXISTS idx_video_stream_created_at ON video_stream (created_at) COMMENT 'Index for time-based queries';
CREATE INDEX IF NOT EXISTS idx_video_stream_status ON video_stream (status) COMMENT 'Index for status filtering';

-- ============================================
-- Table: audio_track (AudioTrack)
-- ============================================
CREATE TABLE IF NOT EXISTS audio_track (
    id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY COMMENT 'Primary Key',
    Integer PK VARCHAR(255) COMMENT 'Integer PK field',
    Integer FK VARCHAR(255) COMMENT 'Integer FK field',
    Integer VARCHAR(255) COMMENT 'Integer field',
    Integer VARCHAR(255) COMMENT 'Integer field',
    JSON VARCHAR(255) COMMENT 'JSON field',
    status VARCHAR(50) NOT NULL DEFAULT 'ACTIVE' COMMENT 'Entity status (ACTIVE, INACTIVE, SUSPENDED)',
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT 'Record creation timestamp',
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT 'Last update timestamp',
    version BIGINT NOT NULL DEFAULT 0 COMMENT 'Optimistic locking version'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='AudioTrack entity table';

CREATE INDEX IF NOT EXISTS idx_audio_track_created_at ON audio_track (created_at) COMMENT 'Index for time-based queries';
CREATE INDEX IF NOT EXISTS idx_audio_track_status ON audio_track (status) COMMENT 'Index for status filtering';

-- ============================================
-- Table: document_preview (DocumentPreview)
-- ============================================
CREATE TABLE IF NOT EXISTS document_preview (
    id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY COMMENT 'Primary Key',
    Integer PK VARCHAR(255) COMMENT 'Integer PK field',
    Integer FK VARCHAR(255) COMMENT 'Integer FK field',
    Integer VARCHAR(255) COMMENT 'Integer field',
    String VARCHAR(255) COMMENT 'String field',
    Text VARCHAR(255) COMMENT 'Text field',
    Boolean VARCHAR(255) COMMENT 'Boolean field',
    status VARCHAR(50) NOT NULL DEFAULT 'ACTIVE' COMMENT 'Entity status (ACTIVE, INACTIVE, SUSPENDED)',
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT 'Record creation timestamp',
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT 'Last update timestamp',
    version BIGINT NOT NULL DEFAULT 0 COMMENT 'Optimistic locking version'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='DocumentPreview entity table';

CREATE INDEX IF NOT EXISTS idx_document_preview_created_at ON document_preview (created_at) COMMENT 'Index for time-based queries';
CREATE INDEX IF NOT EXISTS idx_document_preview_status ON document_preview (status) COMMENT 'Index for status filtering';

-- ============================================
-- Table: bulk_upload (BulkUpload)
-- ============================================
CREATE TABLE IF NOT EXISTS bulk_upload (
    id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY COMMENT 'Primary Key',
    Integer PK VARCHAR(255) COMMENT 'Integer PK field',
    Integer FK VARCHAR(255) COMMENT 'Integer FK field',
    Integer VARCHAR(255) COMMENT 'Integer field',
    Integer VARCHAR(255) COMMENT 'Integer field',
    String VARCHAR(255) COMMENT 'String field',
    DateTime VARCHAR(255) COMMENT 'DateTime field',
    status VARCHAR(50) NOT NULL DEFAULT 'ACTIVE' COMMENT 'Entity status (ACTIVE, INACTIVE, SUSPENDED)',
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT 'Record creation timestamp',
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT 'Last update timestamp',
    version BIGINT NOT NULL DEFAULT 0 COMMENT 'Optimistic locking version'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='BulkUpload entity table';

CREATE INDEX IF NOT EXISTS idx_bulk_upload_created_at ON bulk_upload (created_at) COMMENT 'Index for time-based queries';
CREATE INDEX IF NOT EXISTS idx_bulk_upload_status ON bulk_upload (status) COMMENT 'Index for status filtering';

-- ============================================
-- Table: statistiques (Statistiques)
-- ============================================
CREATE TABLE IF NOT EXISTS statistiques (
    id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY COMMENT 'Primary Key',
    Integer PK VARCHAR(255) COMMENT 'Integer PK field',
    Integer FK VARCHAR(255) COMMENT 'Integer FK field',
    Integer VARCHAR(255) COMMENT 'Integer field',
    Integer VARCHAR(255) COMMENT 'Integer field',
    Integer VARCHAR(255) COMMENT 'Integer field',
    Integer VARCHAR(255) COMMENT 'Integer field',
    Integer VARCHAR(255) COMMENT 'Integer field',
    Integer VARCHAR(255) COMMENT 'Integer field',
    Integer VARCHAR(255) COMMENT 'Integer field',
    Float VARCHAR(255) COMMENT 'Float field',
    Float VARCHAR(255) COMMENT 'Float field',
    DateTime VARCHAR(255) COMMENT 'DateTime field',
    status VARCHAR(50) NOT NULL DEFAULT 'ACTIVE' COMMENT 'Entity status (ACTIVE, INACTIVE, SUSPENDED)',
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT 'Record creation timestamp',
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT 'Last update timestamp',
    version BIGINT NOT NULL DEFAULT 0 COMMENT 'Optimistic locking version'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='Statistiques entity table';

CREATE INDEX IF NOT EXISTS idx_statistiques_created_at ON statistiques (created_at) COMMENT 'Index for time-based queries';
CREATE INDEX IF NOT EXISTS idx_statistiques_status ON statistiques (status) COMMENT 'Index for status filtering';

-- ============================================
-- Table: vue_article (VueArticle)
-- ============================================
CREATE TABLE IF NOT EXISTS vue_article (
    id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY COMMENT 'Primary Key',
    Integer PK VARCHAR(255) COMMENT 'Integer PK field',
    Integer FK VARCHAR(255) COMMENT 'Integer FK field',
    Integer FK VARCHAR(255) COMMENT 'Integer FK field',
    String VARCHAR(255) COMMENT 'String field',
    String VARCHAR(255) COMMENT 'String field',
    String VARCHAR(255) COMMENT 'String field',
    String VARCHAR(255) COMMENT 'String field',
    String VARCHAR(255) COMMENT 'String field',
    Integer VARCHAR(255) COMMENT 'Integer field',
    Integer VARCHAR(255) COMMENT 'Integer field',
    DateTime VARCHAR(255) COMMENT 'DateTime field',
    String VARCHAR(255) COMMENT 'String field',
    status VARCHAR(50) NOT NULL DEFAULT 'ACTIVE' COMMENT 'Entity status (ACTIVE, INACTIVE, SUSPENDED)',
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT 'Record creation timestamp',
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT 'Last update timestamp',
    version BIGINT NOT NULL DEFAULT 0 COMMENT 'Optimistic locking version'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='VueArticle entity table';

CREATE INDEX IF NOT EXISTS idx_vue_article_created_at ON vue_article (created_at) COMMENT 'Index for time-based queries';
CREATE INDEX IF NOT EXISTS idx_vue_article_status ON vue_article (status) COMMENT 'Index for status filtering';

-- ============================================
-- Table: telechargement_media (TelechargementMedia)
-- ============================================
CREATE TABLE IF NOT EXISTS telechargement_media (
    id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY COMMENT 'Primary Key',
    Integer PK VARCHAR(255) COMMENT 'Integer PK field',
    Integer FK VARCHAR(255) COMMENT 'Integer FK field',
    Integer FK VARCHAR(255) COMMENT 'Integer FK field',
    String VARCHAR(255) COMMENT 'String field',
    String VARCHAR(255) COMMENT 'String field',
    DateTime VARCHAR(255) COMMENT 'DateTime field',
    Boolean VARCHAR(255) COMMENT 'Boolean field',
    String VARCHAR(255) COMMENT 'String field',
    status VARCHAR(50) NOT NULL DEFAULT 'ACTIVE' COMMENT 'Entity status (ACTIVE, INACTIVE, SUSPENDED)',
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT 'Record creation timestamp',
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT 'Last update timestamp',
    version BIGINT NOT NULL DEFAULT 0 COMMENT 'Optimistic locking version'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='TelechargementMedia entity table';

CREATE INDEX IF NOT EXISTS idx_telechargement_media_created_at ON telechargement_media (created_at) COMMENT 'Index for time-based queries';
CREATE INDEX IF NOT EXISTS idx_telechargement_media_status ON telechargement_media (status) COMMENT 'Index for status filtering';

-- ============================================
-- Table: partage_article (PartageArticle)
-- ============================================
CREATE TABLE IF NOT EXISTS partage_article (
    id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY COMMENT 'Primary Key',
    Integer PK VARCHAR(255) COMMENT 'Integer PK field',
    Integer FK VARCHAR(255) COMMENT 'Integer FK field',
    Integer FK VARCHAR(255) COMMENT 'Integer FK field',
    DateTime VARCHAR(255) COMMENT 'DateTime field',
    Integer VARCHAR(255) COMMENT 'Integer field',
    status VARCHAR(50) NOT NULL DEFAULT 'ACTIVE' COMMENT 'Entity status (ACTIVE, INACTIVE, SUSPENDED)',
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT 'Record creation timestamp',
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT 'Last update timestamp',
    version BIGINT NOT NULL DEFAULT 0 COMMENT 'Optimistic locking version'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='PartageArticle entity table';

CREATE INDEX IF NOT EXISTS idx_partage_article_created_at ON partage_article (created_at) COMMENT 'Index for time-based queries';
CREATE INDEX IF NOT EXISTS idx_partage_article_status ON partage_article (status) COMMENT 'Index for status filtering';

-- ============================================
-- Table: featured_item (FeaturedItem)
-- ============================================
CREATE TABLE IF NOT EXISTS featured_item (
    id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY COMMENT 'Primary Key',
    Integer PK VARCHAR(255) COMMENT 'Integer PK field',
    Integer FK VARCHAR(255) COMMENT 'Integer FK field',
    Integer VARCHAR(255) COMMENT 'Integer field',
    DateTime VARCHAR(255) COMMENT 'DateTime field',
    DateTime VARCHAR(255) COMMENT 'DateTime field',
    Integer VARCHAR(255) COMMENT 'Integer field',
    Boolean VARCHAR(255) COMMENT 'Boolean field',
    status VARCHAR(50) NOT NULL DEFAULT 'ACTIVE' COMMENT 'Entity status (ACTIVE, INACTIVE, SUSPENDED)',
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT 'Record creation timestamp',
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT 'Last update timestamp',
    version BIGINT NOT NULL DEFAULT 0 COMMENT 'Optimistic locking version'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='FeaturedItem entity table';

CREATE INDEX IF NOT EXISTS idx_featured_item_created_at ON featured_item (created_at) COMMENT 'Index for time-based queries';
CREATE INDEX IF NOT EXISTS idx_featured_item_status ON featured_item (status) COMMENT 'Index for status filtering';

-- ============================================
-- Table: homepage_layout (HomepageLayout)
-- ============================================
CREATE TABLE IF NOT EXISTS homepage_layout (
    id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY COMMENT 'Primary Key',
    Integer PK VARCHAR(255) COMMENT 'Integer PK field',
    Integer VARCHAR(255) COMMENT 'Integer field',
    Integer VARCHAR(255) COMMENT 'Integer field',
    JSON VARCHAR(255) COMMENT 'JSON field',
    Boolean VARCHAR(255) COMMENT 'Boolean field',
    status VARCHAR(50) NOT NULL DEFAULT 'ACTIVE' COMMENT 'Entity status (ACTIVE, INACTIVE, SUSPENDED)',
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT 'Record creation timestamp',
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT 'Last update timestamp',
    version BIGINT NOT NULL DEFAULT 0 COMMENT 'Optimistic locking version'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='HomepageLayout entity table';

CREATE INDEX IF NOT EXISTS idx_homepage_layout_created_at ON homepage_layout (created_at) COMMENT 'Index for time-based queries';
CREATE INDEX IF NOT EXISTS idx_homepage_layout_status ON homepage_layout (status) COMMENT 'Index for status filtering';

-- ============================================
-- Table: boost_rule (BoostRule)
-- ============================================
CREATE TABLE IF NOT EXISTS boost_rule (
    id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY COMMENT 'Primary Key',
    Integer PK VARCHAR(255) COMMENT 'Integer PK field',
    Integer VARCHAR(255) COMMENT 'Integer field',
    Integer VARCHAR(255) COMMENT 'Integer field',
    Boolean VARCHAR(255) COMMENT 'Boolean field',
    Integer FK VARCHAR(255) COMMENT 'Integer FK field',
    DateTime VARCHAR(255) COMMENT 'DateTime field',
    status VARCHAR(50) NOT NULL DEFAULT 'ACTIVE' COMMENT 'Entity status (ACTIVE, INACTIVE, SUSPENDED)',
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT 'Record creation timestamp',
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT 'Last update timestamp',
    version BIGINT NOT NULL DEFAULT 0 COMMENT 'Optimistic locking version'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='BoostRule entity table';

CREATE INDEX IF NOT EXISTS idx_boost_rule_created_at ON boost_rule (created_at) COMMENT 'Index for time-based queries';
CREATE INDEX IF NOT EXISTS idx_boost_rule_status ON boost_rule (status) COMMENT 'Index for status filtering';

-- ============================================
-- Table: category_filter (CategoryFilter)
-- ============================================
CREATE TABLE IF NOT EXISTS category_filter (
    id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY COMMENT 'Primary Key',
    Integer PK VARCHAR(255) COMMENT 'Integer PK field',
    Integer FK VARCHAR(255) COMMENT 'Integer FK field',
    Integer VARCHAR(255) COMMENT 'Integer field',
    JSON VARCHAR(255) COMMENT 'JSON field',
    status VARCHAR(50) NOT NULL DEFAULT 'ACTIVE' COMMENT 'Entity status (ACTIVE, INACTIVE, SUSPENDED)',
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT 'Record creation timestamp',
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT 'Last update timestamp',
    version BIGINT NOT NULL DEFAULT 0 COMMENT 'Optimistic locking version'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='CategoryFilter entity table';

CREATE INDEX IF NOT EXISTS idx_category_filter_created_at ON category_filter (created_at) COMMENT 'Index for time-based queries';
CREATE INDEX IF NOT EXISTS idx_category_filter_status ON category_filter (status) COMMENT 'Index for status filtering';

-- ============================================
-- Table: carousel_slide (CarouselSlide)
-- ============================================
CREATE TABLE IF NOT EXISTS carousel_slide (
    id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY COMMENT 'Primary Key',
    Integer PK VARCHAR(255) COMMENT 'Integer PK field',
    Integer FK VARCHAR(255) COMMENT 'Integer FK field',
    Integer VARCHAR(255) COMMENT 'Integer field',
    Integer VARCHAR(255) COMMENT 'Integer field',
    String VARCHAR(255) COMMENT 'String field',
    status VARCHAR(50) NOT NULL DEFAULT 'ACTIVE' COMMENT 'Entity status (ACTIVE, INACTIVE, SUSPENDED)',
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT 'Record creation timestamp',
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT 'Last update timestamp',
    version BIGINT NOT NULL DEFAULT 0 COMMENT 'Optimistic locking version'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='CarouselSlide entity table';

CREATE INDEX IF NOT EXISTS idx_carousel_slide_created_at ON carousel_slide (created_at) COMMENT 'Index for time-based queries';
CREATE INDEX IF NOT EXISTS idx_carousel_slide_status ON carousel_slide (status) COMMENT 'Index for status filtering';

-- ============================================
-- Table: card_preview (CardPreview)
-- ============================================
CREATE TABLE IF NOT EXISTS card_preview (
    id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY COMMENT 'Primary Key',
    Integer PK VARCHAR(255) COMMENT 'Integer PK field',
    Integer FK VARCHAR(255) COMMENT 'Integer FK field',
    JSON VARCHAR(255) COMMENT 'JSON field',
    Integer VARCHAR(255) COMMENT 'Integer field',
    status VARCHAR(50) NOT NULL DEFAULT 'ACTIVE' COMMENT 'Entity status (ACTIVE, INACTIVE, SUSPENDED)',
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT 'Record creation timestamp',
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT 'Last update timestamp',
    version BIGINT NOT NULL DEFAULT 0 COMMENT 'Optimistic locking version'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='CardPreview entity table';

CREATE INDEX IF NOT EXISTS idx_card_preview_created_at ON card_preview (created_at) COMMENT 'Index for time-based queries';
CREATE INDEX IF NOT EXISTS idx_card_preview_status ON card_preview (status) COMMENT 'Index for status filtering';

-- ============================================
-- Table: real_time_update (RealTimeUpdate)
-- ============================================
CREATE TABLE IF NOT EXISTS real_time_update (
    id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY COMMENT 'Primary Key',
    Integer PK VARCHAR(255) COMMENT 'Integer PK field',
    JSON VARCHAR(255) COMMENT 'JSON field',
    DateTime VARCHAR(255) COMMENT 'DateTime field',
    status VARCHAR(50) NOT NULL DEFAULT 'ACTIVE' COMMENT 'Entity status (ACTIVE, INACTIVE, SUSPENDED)',
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT 'Record creation timestamp',
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT 'Last update timestamp',
    version BIGINT NOT NULL DEFAULT 0 COMMENT 'Optimistic locking version'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='RealTimeUpdate entity table';

CREATE INDEX IF NOT EXISTS idx_real_time_update_created_at ON real_time_update (created_at) COMMENT 'Index for time-based queries';
CREATE INDEX IF NOT EXISTS idx_real_time_update_status ON real_time_update (status) COMMENT 'Index for status filtering';

-- ============================================
-- Table: user_preference (UserPreference)
-- ============================================
CREATE TABLE IF NOT EXISTS user_preference (
    id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY COMMENT 'Primary Key',
    Integer PK VARCHAR(255) COMMENT 'Integer PK field',
    Integer FK VARCHAR(255) COMMENT 'Integer FK field',
    JSON VARCHAR(255) COMMENT 'JSON field',
    String VARCHAR(255) COMMENT 'String field',
    String VARCHAR(255) COMMENT 'String field',
    status VARCHAR(50) NOT NULL DEFAULT 'ACTIVE' COMMENT 'Entity status (ACTIVE, INACTIVE, SUSPENDED)',
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT 'Record creation timestamp',
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT 'Last update timestamp',
    version BIGINT NOT NULL DEFAULT 0 COMMENT 'Optimistic locking version'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='UserPreference entity table';

CREATE INDEX IF NOT EXISTS idx_user_preference_created_at ON user_preference (created_at) COMMENT 'Index for time-based queries';
CREATE INDEX IF NOT EXISTS idx_user_preference_status ON user_preference (status) COMMENT 'Index for status filtering';

-- ============================================
-- Table: seoconfig (SEOConfig)
-- ============================================
CREATE TABLE IF NOT EXISTS seoconfig (
    id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY COMMENT 'Primary Key',
    Integer PK VARCHAR(255) COMMENT 'Integer PK field',
    Integer FK VARCHAR(255) COMMENT 'Integer FK field',
    Integer FK VARCHAR(255) COMMENT 'Integer FK field',
    String VARCHAR(255) COMMENT 'String field',
    String VARCHAR(255) COMMENT 'String field',
    String VARCHAR(255) COMMENT 'String field',
    JSON VARCHAR(255) COMMENT 'JSON field',
    String VARCHAR(255) COMMENT 'String field',
    Boolean VARCHAR(255) COMMENT 'Boolean field',
    Boolean VARCHAR(255) COMMENT 'Boolean field',
    String VARCHAR(255) COMMENT 'String field',
    String VARCHAR(255) COMMENT 'String field',
    status VARCHAR(50) NOT NULL DEFAULT 'ACTIVE' COMMENT 'Entity status (ACTIVE, INACTIVE, SUSPENDED)',
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT 'Record creation timestamp',
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT 'Last update timestamp',
    version BIGINT NOT NULL DEFAULT 0 COMMENT 'Optimistic locking version'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='SEOConfig entity table';

CREATE INDEX IF NOT EXISTS idx_seoconfig_created_at ON seoconfig (created_at) COMMENT 'Index for time-based queries';
CREATE INDEX IF NOT EXISTS idx_seoconfig_status ON seoconfig (status) COMMENT 'Index for status filtering';

-- ============================================
-- Table: sitemap (Sitemap)
-- ============================================
CREATE TABLE IF NOT EXISTS sitemap (
    id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY COMMENT 'Primary Key',
    Integer PK VARCHAR(255) COMMENT 'Integer PK field',
    DateTime VARCHAR(255) COMMENT 'DateTime field',
    String VARCHAR(255) COMMENT 'String field',
    Float VARCHAR(255) COMMENT 'Float field',
    Integer VARCHAR(255) COMMENT 'Integer field',
    status VARCHAR(50) NOT NULL DEFAULT 'ACTIVE' COMMENT 'Entity status (ACTIVE, INACTIVE, SUSPENDED)',
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT 'Record creation timestamp',
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT 'Last update timestamp',
    version BIGINT NOT NULL DEFAULT 0 COMMENT 'Optimistic locking version'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='Sitemap entity table';

CREATE INDEX IF NOT EXISTS idx_sitemap_created_at ON sitemap (created_at) COMMENT 'Index for time-based queries';
CREATE INDEX IF NOT EXISTS idx_sitemap_status ON sitemap (status) COMMENT 'Index for status filtering';

-- ============================================
-- Table: page (Page)
-- ============================================
CREATE TABLE IF NOT EXISTS page (
    id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY COMMENT 'Primary Key',
    Integer PK VARCHAR(255) COMMENT 'Integer PK field',
    String VARCHAR(255) COMMENT 'String field',
    String VARCHAR(255) COMMENT 'String field',
    Integer FK VARCHAR(255) COMMENT 'Integer FK field',
    JSON VARCHAR(255) COMMENT 'JSON field',
    Text VARCHAR(255) COMMENT 'Text field',
    Boolean VARCHAR(255) COMMENT 'Boolean field',
    status VARCHAR(50) NOT NULL DEFAULT 'ACTIVE' COMMENT 'Entity status (ACTIVE, INACTIVE, SUSPENDED)',
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT 'Record creation timestamp',
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT 'Last update timestamp',
    version BIGINT NOT NULL DEFAULT 0 COMMENT 'Optimistic locking version'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='Page entity table';

CREATE INDEX IF NOT EXISTS idx_page_created_at ON page (created_at) COMMENT 'Index for time-based queries';
CREATE INDEX IF NOT EXISTS idx_page_status ON page (status) COMMENT 'Index for status filtering';

-- ============================================
-- Table: menu (Menu)
-- ============================================
CREATE TABLE IF NOT EXISTS menu (
    id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY COMMENT 'Primary Key',
    Integer PK VARCHAR(255) COMMENT 'Integer PK field',
    JSON VARCHAR(255) COMMENT 'JSON field',
    Integer FK VARCHAR(255) COMMENT 'Integer FK field',
    Integer VARCHAR(255) COMMENT 'Integer field',
    Boolean VARCHAR(255) COMMENT 'Boolean field',
    status VARCHAR(50) NOT NULL DEFAULT 'ACTIVE' COMMENT 'Entity status (ACTIVE, INACTIVE, SUSPENDED)',
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT 'Record creation timestamp',
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT 'Last update timestamp',
    version BIGINT NOT NULL DEFAULT 0 COMMENT 'Optimistic locking version'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='Menu entity table';

CREATE INDEX IF NOT EXISTS idx_menu_created_at ON menu (created_at) COMMENT 'Index for time-based queries';
CREATE INDEX IF NOT EXISTS idx_menu_status ON menu (status) COMMENT 'Index for status filtering';

-- ============================================
-- Table: langue (Langue)
-- ============================================
CREATE TABLE IF NOT EXISTS langue (
    id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY COMMENT 'Primary Key',
    Integer PK VARCHAR(255) COMMENT 'Integer PK field',
    String VARCHAR(255) COMMENT 'String field',
    String VARCHAR(255) COMMENT 'String field',
    Boolean VARCHAR(255) COMMENT 'Boolean field',
    String VARCHAR(255) COMMENT 'String field',
    status VARCHAR(50) NOT NULL DEFAULT 'ACTIVE' COMMENT 'Entity status (ACTIVE, INACTIVE, SUSPENDED)',
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT 'Record creation timestamp',
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT 'Last update timestamp',
    version BIGINT NOT NULL DEFAULT 0 COMMENT 'Optimistic locking version'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='Langue entity table';

CREATE INDEX IF NOT EXISTS idx_langue_created_at ON langue (created_at) COMMENT 'Index for time-based queries';
CREATE INDEX IF NOT EXISTS idx_langue_status ON langue (status) COMMENT 'Index for status filtering';

-- ============================================
-- Table: traduction (Traduction)
-- ============================================
CREATE TABLE IF NOT EXISTS traduction (
    id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY COMMENT 'Primary Key',
    Integer PK VARCHAR(255) COMMENT 'Integer PK field',
    Integer FK VARCHAR(255) COMMENT 'Integer FK field',
    Integer FK VARCHAR(255) COMMENT 'Integer FK field',
    String VARCHAR(255) COMMENT 'String field',
    JSON VARCHAR(255) COMMENT 'JSON field',
    Integer FK VARCHAR(255) COMMENT 'Integer FK field',
    Integer VARCHAR(255) COMMENT 'Integer field',
    status VARCHAR(50) NOT NULL DEFAULT 'ACTIVE' COMMENT 'Entity status (ACTIVE, INACTIVE, SUSPENDED)',
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT 'Record creation timestamp',
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT 'Last update timestamp',
    version BIGINT NOT NULL DEFAULT 0 COMMENT 'Optimistic locking version'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='Traduction entity table';

CREATE INDEX IF NOT EXISTS idx_traduction_created_at ON traduction (created_at) COMMENT 'Index for time-based queries';
CREATE INDEX IF NOT EXISTS idx_traduction_status ON traduction (status) COMMENT 'Index for status filtering';

-- ============================================
-- Table: langue_contenu (LangueContenu)
-- ============================================
CREATE TABLE IF NOT EXISTS langue_contenu (
    id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY COMMENT 'Primary Key',
    Integer PK VARCHAR(255) COMMENT 'Integer PK field',
    Integer FK VARCHAR(255) COMMENT 'Integer FK field',
    Integer FK VARCHAR(255) COMMENT 'Integer FK field',
    Integer VARCHAR(255) COMMENT 'Integer field',
    Boolean VARCHAR(255) COMMENT 'Boolean field',
    status VARCHAR(50) NOT NULL DEFAULT 'ACTIVE' COMMENT 'Entity status (ACTIVE, INACTIVE, SUSPENDED)',
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT 'Record creation timestamp',
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT 'Last update timestamp',
    version BIGINT NOT NULL DEFAULT 0 COMMENT 'Optimistic locking version'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='LangueContenu entity table';

CREATE INDEX IF NOT EXISTS idx_langue_contenu_created_at ON langue_contenu (created_at) COMMENT 'Index for time-based queries';
CREATE INDEX IF NOT EXISTS idx_langue_contenu_status ON langue_contenu (status) COMMENT 'Index for status filtering';

-- ============================================
-- Table: pwaconfig (PWAConfig)
-- ============================================
CREATE TABLE IF NOT EXISTS pwaconfig (
    id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY COMMENT 'Primary Key',
    Integer PK VARCHAR(255) COMMENT 'Integer PK field',
    JSON VARCHAR(255) COMMENT 'JSON field',
    String VARCHAR(255) COMMENT 'String field',
    JSON VARCHAR(255) COMMENT 'JSON field',
    Boolean VARCHAR(255) COMMENT 'Boolean field',
    status VARCHAR(50) NOT NULL DEFAULT 'ACTIVE' COMMENT 'Entity status (ACTIVE, INACTIVE, SUSPENDED)',
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT 'Record creation timestamp',
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT 'Last update timestamp',
    version BIGINT NOT NULL DEFAULT 0 COMMENT 'Optimistic locking version'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='PWAConfig entity table';

CREATE INDEX IF NOT EXISTS idx_pwaconfig_created_at ON pwaconfig (created_at) COMMENT 'Index for time-based queries';
CREATE INDEX IF NOT EXISTS idx_pwaconfig_status ON pwaconfig (status) COMMENT 'Index for status filtering';

-- ============================================
-- Table: analytics_event (AnalyticsEvent)
-- ============================================
CREATE TABLE IF NOT EXISTS analytics_event (
    id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY COMMENT 'Primary Key',
    Integer PK VARCHAR(255) COMMENT 'Integer PK field',
    Integer FK VARCHAR(255) COMMENT 'Integer FK field',
    String VARCHAR(255) COMMENT 'String field',
    JSON VARCHAR(255) COMMENT 'JSON field',
    DateTime VARCHAR(255) COMMENT 'DateTime field',
    JSON VARCHAR(255) COMMENT 'JSON field',
    status VARCHAR(50) NOT NULL DEFAULT 'ACTIVE' COMMENT 'Entity status (ACTIVE, INACTIVE, SUSPENDED)',
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT 'Record creation timestamp',
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT 'Last update timestamp',
    version BIGINT NOT NULL DEFAULT 0 COMMENT 'Optimistic locking version'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='AnalyticsEvent entity table';

CREATE INDEX IF NOT EXISTS idx_analytics_event_created_at ON analytics_event (created_at) COMMENT 'Index for time-based queries';
CREATE INDEX IF NOT EXISTS idx_analytics_event_status ON analytics_event (status) COMMENT 'Index for status filtering';

-- ============================================
-- Table: abtest (ABTest)
-- ============================================
CREATE TABLE IF NOT EXISTS abtest (
    id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY COMMENT 'Primary Key',
    Integer PK VARCHAR(255) COMMENT 'Integer PK field',
    Integer FK VARCHAR(255) COMMENT 'Integer FK field',
    String VARCHAR(255) COMMENT 'String field',
    JSON VARCHAR(255) COMMENT 'JSON field',
    JSON VARCHAR(255) COMMENT 'JSON field',
    DateTime VARCHAR(255) COMMENT 'DateTime field',
    DateTime VARCHAR(255) COMMENT 'DateTime field',
    Integer VARCHAR(255) COMMENT 'Integer field',
    status VARCHAR(50) NOT NULL DEFAULT 'ACTIVE' COMMENT 'Entity status (ACTIVE, INACTIVE, SUSPENDED)',
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT 'Record creation timestamp',
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT 'Last update timestamp',
    version BIGINT NOT NULL DEFAULT 0 COMMENT 'Optimistic locking version'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='ABTest entity table';

CREATE INDEX IF NOT EXISTS idx_abtest_created_at ON abtest (created_at) COMMENT 'Index for time-based queries';
CREATE INDEX IF NOT EXISTS idx_abtest_status ON abtest (status) COMMENT 'Index for status filtering';

-- ============================================
-- Table: produit_premium (ProduitPremium)
-- ============================================
CREATE TABLE IF NOT EXISTS produit_premium (
    id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY COMMENT 'Primary Key',
    Integer PK VARCHAR(255) COMMENT 'Integer PK field',
    Integer FK VARCHAR(255) COMMENT 'Integer FK field',
    Integer VARCHAR(255) COMMENT 'Integer field',
    Boolean VARCHAR(255) COMMENT 'Boolean field',
    Integer VARCHAR(255) COMMENT 'Integer field',
    Integer VARCHAR(255) COMMENT 'Integer field',
    status VARCHAR(50) NOT NULL DEFAULT 'ACTIVE' COMMENT 'Entity status (ACTIVE, INACTIVE, SUSPENDED)',
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT 'Record creation timestamp',
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT 'Last update timestamp',
    version BIGINT NOT NULL DEFAULT 0 COMMENT 'Optimistic locking version'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='ProduitPremium entity table';

CREATE INDEX IF NOT EXISTS idx_produit_premium_created_at ON produit_premium (created_at) COMMENT 'Index for time-based queries';
CREATE INDEX IF NOT EXISTS idx_produit_premium_status ON produit_premium (status) COMMENT 'Index for status filtering';

-- ============================================
-- Table: panier (Panier)
-- ============================================
CREATE TABLE IF NOT EXISTS panier (
    id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY COMMENT 'Primary Key',
    Integer PK VARCHAR(255) COMMENT 'Integer PK field',
    Integer FK VARCHAR(255) COMMENT 'Integer FK field',
    String VARCHAR(255) COMMENT 'String field',
    JSON VARCHAR(255) COMMENT 'JSON field',
    Integer VARCHAR(255) COMMENT 'Integer field',
    DateTime VARCHAR(255) COMMENT 'DateTime field',
    DateTime VARCHAR(255) COMMENT 'DateTime field',
    status VARCHAR(50) NOT NULL DEFAULT 'ACTIVE' COMMENT 'Entity status (ACTIVE, INACTIVE, SUSPENDED)',
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT 'Record creation timestamp',
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT 'Last update timestamp',
    version BIGINT NOT NULL DEFAULT 0 COMMENT 'Optimistic locking version'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='Panier entity table';

CREATE INDEX IF NOT EXISTS idx_panier_created_at ON panier (created_at) COMMENT 'Index for time-based queries';
CREATE INDEX IF NOT EXISTS idx_panier_status ON panier (status) COMMENT 'Index for status filtering';

-- ============================================
-- Table: paiement (Paiement)
-- ============================================
CREATE TABLE IF NOT EXISTS paiement (
    id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY COMMENT 'Primary Key',
    Integer PK VARCHAR(255) COMMENT 'Integer PK field',
    Integer FK VARCHAR(255) COMMENT 'Integer FK field',
    Integer FK VARCHAR(255) COMMENT 'Integer FK field',
    Integer VARCHAR(255) COMMENT 'Integer field',
    String VARCHAR(255) COMMENT 'String field',
    String VARCHAR(255) COMMENT 'String field',
    DateTime VARCHAR(255) COMMENT 'DateTime field',
    status VARCHAR(50) NOT NULL DEFAULT 'ACTIVE' COMMENT 'Entity status (ACTIVE, INACTIVE, SUSPENDED)',
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT 'Record creation timestamp',
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT 'Last update timestamp',
    version BIGINT NOT NULL DEFAULT 0 COMMENT 'Optimistic locking version'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='Paiement entity table';

CREATE INDEX IF NOT EXISTS idx_paiement_created_at ON paiement (created_at) COMMENT 'Index for time-based queries';
CREATE INDEX IF NOT EXISTS idx_paiement_status ON paiement (status) COMMENT 'Index for status filtering';

-- ============================================
-- Table: transaction (Transaction)
-- ============================================
CREATE TABLE IF NOT EXISTS transaction (
    id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY COMMENT 'Primary Key',
    Integer PK VARCHAR(255) COMMENT 'Integer PK field',
    Integer FK VARCHAR(255) COMMENT 'Integer FK field',
    Integer FK VARCHAR(255) COMMENT 'Integer FK field',
    Integer FK VARCHAR(255) COMMENT 'Integer FK field',
    Integer VARCHAR(255) COMMENT 'Integer field',
    String VARCHAR(255) COMMENT 'String field',
    String VARCHAR(255) COMMENT 'String field',
    String VARCHAR(255) COMMENT 'String field',
    DateTime VARCHAR(255) COMMENT 'DateTime field',
    JSON VARCHAR(255) COMMENT 'JSON field',
    status VARCHAR(50) NOT NULL DEFAULT 'ACTIVE' COMMENT 'Entity status (ACTIVE, INACTIVE, SUSPENDED)',
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT 'Record creation timestamp',
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT 'Last update timestamp',
    version BIGINT NOT NULL DEFAULT 0 COMMENT 'Optimistic locking version'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='Transaction entity table';

CREATE INDEX IF NOT EXISTS idx_transaction_created_at ON transaction (created_at) COMMENT 'Index for time-based queries';
CREATE INDEX IF NOT EXISTS idx_transaction_status ON transaction (status) COMMENT 'Index for status filtering';

-- ============================================
-- Table: abonnement_payant (AbonnementPayant)
-- ============================================
CREATE TABLE IF NOT EXISTS abonnement_payant (
    id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY COMMENT 'Primary Key',
    Integer PK VARCHAR(255) COMMENT 'Integer PK field',
    Integer FK VARCHAR(255) COMMENT 'Integer FK field',
    DateTime VARCHAR(255) COMMENT 'DateTime field',
    DateTime VARCHAR(255) COMMENT 'DateTime field',
    Integer VARCHAR(255) COMMENT 'Integer field',
    Boolean VARCHAR(255) COMMENT 'Boolean field',
    status VARCHAR(50) NOT NULL DEFAULT 'ACTIVE' COMMENT 'Entity status (ACTIVE, INACTIVE, SUSPENDED)',
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT 'Record creation timestamp',
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT 'Last update timestamp',
    version BIGINT NOT NULL DEFAULT 0 COMMENT 'Optimistic locking version'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='AbonnementPayant entity table';

CREATE INDEX IF NOT EXISTS idx_abonnement_payant_created_at ON abonnement_payant (created_at) COMMENT 'Index for time-based queries';
CREATE INDEX IF NOT EXISTS idx_abonnement_payant_status ON abonnement_payant (status) COMMENT 'Index for status filtering';

-- ============================================
-- Table: paywall (Paywall)
-- ============================================
CREATE TABLE IF NOT EXISTS paywall (
    id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY COMMENT 'Primary Key',
    Integer PK VARCHAR(255) COMMENT 'Integer PK field',
    Integer FK VARCHAR(255) COMMENT 'Integer FK field',
    Integer VARCHAR(255) COMMENT 'Integer field',
    String VARCHAR(255) COMMENT 'String field',
    Boolean VARCHAR(255) COMMENT 'Boolean field',
    Text VARCHAR(255) COMMENT 'Text field',
    status VARCHAR(50) NOT NULL DEFAULT 'ACTIVE' COMMENT 'Entity status (ACTIVE, INACTIVE, SUSPENDED)',
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT 'Record creation timestamp',
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT 'Last update timestamp',
    version BIGINT NOT NULL DEFAULT 0 COMMENT 'Optimistic locking version'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='Paywall entity table';

CREATE INDEX IF NOT EXISTS idx_paywall_created_at ON paywall (created_at) COMMENT 'Index for time-based queries';
CREATE INDEX IF NOT EXISTS idx_paywall_status ON paywall (status) COMMENT 'Index for status filtering';

-- ============================================
-- Table: commentaire (Commentaire)
-- ============================================
CREATE TABLE IF NOT EXISTS commentaire (
    id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY COMMENT 'Primary Key',
    Integer PK VARCHAR(255) COMMENT 'Integer PK field',
    Integer FK VARCHAR(255) COMMENT 'Integer FK field',
    Integer FK VARCHAR(255) COMMENT 'Integer FK field',
    Integer FK VARCHAR(255) COMMENT 'Integer FK field',
    Text VARCHAR(255) COMMENT 'Text field',
    DateTime VARCHAR(255) COMMENT 'DateTime field',
    DateTime VARCHAR(255) COMMENT 'DateTime field',
    Integer VARCHAR(255) COMMENT 'Integer field',
    Float VARCHAR(255) COMMENT 'Float field',
    status VARCHAR(50) NOT NULL DEFAULT 'ACTIVE' COMMENT 'Entity status (ACTIVE, INACTIVE, SUSPENDED)',
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT 'Record creation timestamp',
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT 'Last update timestamp',
    version BIGINT NOT NULL DEFAULT 0 COMMENT 'Optimistic locking version'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='Commentaire entity table';

CREATE INDEX IF NOT EXISTS idx_commentaire_created_at ON commentaire (created_at) COMMENT 'Index for time-based queries';
CREATE INDEX IF NOT EXISTS idx_commentaire_status ON commentaire (status) COMMENT 'Index for status filtering';

-- ============================================
-- Table: favori (Favori)
-- ============================================
CREATE TABLE IF NOT EXISTS favori (
    id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY COMMENT 'Primary Key',
    Integer PK VARCHAR(255) COMMENT 'Integer PK field',
    Integer FK VARCHAR(255) COMMENT 'Integer FK field',
    Integer FK VARCHAR(255) COMMENT 'Integer FK field',
    DateTime VARCHAR(255) COMMENT 'DateTime field',
    String VARCHAR(255) COMMENT 'String field',
    Text VARCHAR(255) COMMENT 'Text field',
    status VARCHAR(50) NOT NULL DEFAULT 'ACTIVE' COMMENT 'Entity status (ACTIVE, INACTIVE, SUSPENDED)',
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT 'Record creation timestamp',
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT 'Last update timestamp',
    version BIGINT NOT NULL DEFAULT 0 COMMENT 'Optimistic locking version'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='Favori entity table';

CREATE INDEX IF NOT EXISTS idx_favori_created_at ON favori (created_at) COMMENT 'Index for time-based queries';
CREATE INDEX IF NOT EXISTS idx_favori_status ON favori (status) COMMENT 'Index for status filtering';

-- ============================================
-- Table: abonnement_newsletter (AbonnementNewsletter)
-- ============================================
CREATE TABLE IF NOT EXISTS abonnement_newsletter (
    id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY COMMENT 'Primary Key',
    Integer PK VARCHAR(255) COMMENT 'Integer PK field',
    String VARCHAR(255) COMMENT 'String field',
    JSON VARCHAR(255) COMMENT 'JSON field',
    String VARCHAR(255) COMMENT 'String field',
    DateTime VARCHAR(255) COMMENT 'DateTime field',
    String VARCHAR(255) COMMENT 'String field',
    Boolean VARCHAR(255) COMMENT 'Boolean field',
    status VARCHAR(50) NOT NULL DEFAULT 'ACTIVE' COMMENT 'Entity status (ACTIVE, INACTIVE, SUSPENDED)',
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT 'Record creation timestamp',
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT 'Last update timestamp',
    version BIGINT NOT NULL DEFAULT 0 COMMENT 'Optimistic locking version'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='AbonnementNewsletter entity table';

CREATE INDEX IF NOT EXISTS idx_abonnement_newsletter_created_at ON abonnement_newsletter (created_at) COMMENT 'Index for time-based queries';
CREATE INDEX IF NOT EXISTS idx_abonnement_newsletter_status ON abonnement_newsletter (status) COMMENT 'Index for status filtering';

-- ============================================
-- Table: recommandation (Recommandation)
-- ============================================
CREATE TABLE IF NOT EXISTS recommandation (
    id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY COMMENT 'Primary Key',
    Integer PK VARCHAR(255) COMMENT 'Integer PK field',
    Integer FK VARCHAR(255) COMMENT 'Integer FK field',
    Integer FK VARCHAR(255) COMMENT 'Integer FK field',
    Float VARCHAR(255) COMMENT 'Float field',
    String VARCHAR(255) COMMENT 'String field',
    DateTime VARCHAR(255) COMMENT 'DateTime field',
    Boolean VARCHAR(255) COMMENT 'Boolean field',
    Boolean VARCHAR(255) COMMENT 'Boolean field',
    status VARCHAR(50) NOT NULL DEFAULT 'ACTIVE' COMMENT 'Entity status (ACTIVE, INACTIVE, SUSPENDED)',
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT 'Record creation timestamp',
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT 'Last update timestamp',
    version BIGINT NOT NULL DEFAULT 0 COMMENT 'Optimistic locking version'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='Recommandation entity table';

CREATE INDEX IF NOT EXISTS idx_recommandation_created_at ON recommandation (created_at) COMMENT 'Index for time-based queries';
CREATE INDEX IF NOT EXISTS idx_recommandation_status ON recommandation (status) COMMENT 'Index for status filtering';

-- ============================================
-- Table: ssoconfig (SSOConfig)
-- ============================================
CREATE TABLE IF NOT EXISTS ssoconfig (
    id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY COMMENT 'Primary Key',
    Integer PK VARCHAR(255) COMMENT 'Integer PK field',
    String VARCHAR(255) COMMENT 'String field',
    String VARCHAR(255) COMMENT 'String field',
    String VARCHAR(255) COMMENT 'String field',
    Boolean VARCHAR(255) COMMENT 'Boolean field',
    JSON VARCHAR(255) COMMENT 'JSON field',
    status VARCHAR(50) NOT NULL DEFAULT 'ACTIVE' COMMENT 'Entity status (ACTIVE, INACTIVE, SUSPENDED)',
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT 'Record creation timestamp',
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT 'Last update timestamp',
    version BIGINT NOT NULL DEFAULT 0 COMMENT 'Optimistic locking version'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='SSOConfig entity table';

CREATE INDEX IF NOT EXISTS idx_ssoconfig_created_at ON ssoconfig (created_at) COMMENT 'Index for time-based queries';
CREATE INDEX IF NOT EXISTS idx_ssoconfig_status ON ssoconfig (status) COMMENT 'Index for status filtering';

-- ============================================
-- Table: rate_limit (RateLimit)
-- ============================================
CREATE TABLE IF NOT EXISTS rate_limit (
    id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY COMMENT 'Primary Key',
    Integer PK VARCHAR(255) COMMENT 'Integer PK field',
    String VARCHAR(255) COMMENT 'String field',
    String VARCHAR(255) COMMENT 'String field',
    Integer VARCHAR(255) COMMENT 'Integer field',
    Integer VARCHAR(255) COMMENT 'Integer field',
    DateTime VARCHAR(255) COMMENT 'DateTime field',
    DateTime VARCHAR(255) COMMENT 'DateTime field',
    status VARCHAR(50) NOT NULL DEFAULT 'ACTIVE' COMMENT 'Entity status (ACTIVE, INACTIVE, SUSPENDED)',
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT 'Record creation timestamp',
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT 'Last update timestamp',
    version BIGINT NOT NULL DEFAULT 0 COMMENT 'Optimistic locking version'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='RateLimit entity table';

CREATE INDEX IF NOT EXISTS idx_rate_limit_created_at ON rate_limit (created_at) COMMENT 'Index for time-based queries';
CREATE INDEX IF NOT EXISTS idx_rate_limit_status ON rate_limit (status) COMMENT 'Index for status filtering';

-- ============================================
-- Table: wcagaudit (WCAGAudit)
-- ============================================
CREATE TABLE IF NOT EXISTS wcagaudit (
    id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY COMMENT 'Primary Key',
    Integer PK VARCHAR(255) COMMENT 'Integer PK field',
    Integer FK VARCHAR(255) COMMENT 'Integer FK field',
    Integer VARCHAR(255) COMMENT 'Integer field',
    Integer VARCHAR(255) COMMENT 'Integer field',
    JSON VARCHAR(255) COMMENT 'JSON field',
    DateTime VARCHAR(255) COMMENT 'DateTime field',
    Boolean VARCHAR(255) COMMENT 'Boolean field',
    status VARCHAR(50) NOT NULL DEFAULT 'ACTIVE' COMMENT 'Entity status (ACTIVE, INACTIVE, SUSPENDED)',
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT 'Record creation timestamp',
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT 'Last update timestamp',
    version BIGINT NOT NULL DEFAULT 0 COMMENT 'Optimistic locking version'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='WCAGAudit entity table';

CREATE INDEX IF NOT EXISTS idx_wcagaudit_created_at ON wcagaudit (created_at) COMMENT 'Index for time-based queries';
CREATE INDEX IF NOT EXISTS idx_wcagaudit_status ON wcagaudit (status) COMMENT 'Index for status filtering';

-- ============================================
-- Table: cdnconfig (CDNConfig)
-- ============================================
CREATE TABLE IF NOT EXISTS cdnconfig (
    id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY COMMENT 'Primary Key',
    Integer PK VARCHAR(255) COMMENT 'Integer PK field',
    JSON VARCHAR(255) COMMENT 'JSON field',
    String VARCHAR(255) COMMENT 'String field',
    Integer VARCHAR(255) COMMENT 'Integer field',
    Boolean VARCHAR(255) COMMENT 'Boolean field',
    status VARCHAR(50) NOT NULL DEFAULT 'ACTIVE' COMMENT 'Entity status (ACTIVE, INACTIVE, SUSPENDED)',
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT 'Record creation timestamp',
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT 'Last update timestamp',
    version BIGINT NOT NULL DEFAULT 0 COMMENT 'Optimistic locking version'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='CDNConfig entity table';

CREATE INDEX IF NOT EXISTS idx_cdnconfig_created_at ON cdnconfig (created_at) COMMENT 'Index for time-based queries';
CREATE INDEX IF NOT EXISTS idx_cdnconfig_status ON cdnconfig (status) COMMENT 'Index for status filtering';

-- ============================================
-- Table: ticket_support (TicketSupport)
-- ============================================
CREATE TABLE IF NOT EXISTS ticket_support (
    id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY COMMENT 'Primary Key',
    Integer PK VARCHAR(255) COMMENT 'Integer PK field',
    Integer FK VARCHAR(255) COMMENT 'Integer FK field',
    String VARCHAR(255) COMMENT 'String field',
    Text VARCHAR(255) COMMENT 'Text field',
    Integer VARCHAR(255) COMMENT 'Integer field',
    Integer FK VARCHAR(255) COMMENT 'Integer FK field',
    DateTime VARCHAR(255) COMMENT 'DateTime field',
    DateTime VARCHAR(255) COMMENT 'DateTime field',
    DateTime VARCHAR(255) COMMENT 'DateTime field',
    status VARCHAR(50) NOT NULL DEFAULT 'ACTIVE' COMMENT 'Entity status (ACTIVE, INACTIVE, SUSPENDED)',
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT 'Record creation timestamp',
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT 'Last update timestamp',
    version BIGINT NOT NULL DEFAULT 0 COMMENT 'Optimistic locking version'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='TicketSupport entity table';

CREATE INDEX IF NOT EXISTS idx_ticket_support_created_at ON ticket_support (created_at) COMMENT 'Index for time-based queries';
CREATE INDEX IF NOT EXISTS idx_ticket_support_status ON ticket_support (status) COMMENT 'Index for status filtering';

-- ============================================
-- Table: maintenance_task (MaintenanceTask)
-- ============================================
CREATE TABLE IF NOT EXISTS maintenance_task (
    id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY COMMENT 'Primary Key',
    Integer PK VARCHAR(255) COMMENT 'Integer PK field',
    Text VARCHAR(255) COMMENT 'Text field',
    DateTime VARCHAR(255) COMMENT 'DateTime field',
    DateTime VARCHAR(255) COMMENT 'DateTime field',
    Float VARCHAR(255) COMMENT 'Float field',
    Integer FK VARCHAR(255) COMMENT 'Integer FK field',
    status VARCHAR(50) NOT NULL DEFAULT 'ACTIVE' COMMENT 'Entity status (ACTIVE, INACTIVE, SUSPENDED)',
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT 'Record creation timestamp',
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT 'Last update timestamp',
    version BIGINT NOT NULL DEFAULT 0 COMMENT 'Optimistic locking version'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='MaintenanceTask entity table';

CREATE INDEX IF NOT EXISTS idx_maintenance_task_created_at ON maintenance_task (created_at) COMMENT 'Index for time-based queries';
CREATE INDEX IF NOT EXISTS idx_maintenance_task_status ON maintenance_task (status) COMMENT 'Index for status filtering';

-- ============================================
-- Table: backup (Backup)
-- ============================================
CREATE TABLE IF NOT EXISTS backup (
    id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY COMMENT 'Primary Key',
    Integer PK VARCHAR(255) COMMENT 'Integer PK field',
    String VARCHAR(255) COMMENT 'String field',
    Integer VARCHAR(255) COMMENT 'Integer field',
    DateTime VARCHAR(255) COMMENT 'DateTime field',
    Boolean VARCHAR(255) COMMENT 'Boolean field',
    Integer VARCHAR(255) COMMENT 'Integer field',
    Integer VARCHAR(255) COMMENT 'Integer field',
    String VARCHAR(255) COMMENT 'String field',
    status VARCHAR(50) NOT NULL DEFAULT 'ACTIVE' COMMENT 'Entity status (ACTIVE, INACTIVE, SUSPENDED)',
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT 'Record creation timestamp',
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT 'Last update timestamp',
    version BIGINT NOT NULL DEFAULT 0 COMMENT 'Optimistic locking version'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='Backup entity table';

CREATE INDEX IF NOT EXISTS idx_backup_created_at ON backup (created_at) COMMENT 'Index for time-based queries';
CREATE INDEX IF NOT EXISTS idx_backup_status ON backup (status) COMMENT 'Index for status filtering';

-- ============================================
-- Table: system_log (SystemLog)
-- ============================================
CREATE TABLE IF NOT EXISTS system_log (
    id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY COMMENT 'Primary Key',
    Integer PK VARCHAR(255) COMMENT 'Integer PK field',
    Text VARCHAR(255) COMMENT 'Text field',
    DateTime VARCHAR(255) COMMENT 'DateTime field',
    Integer FK VARCHAR(255) COMMENT 'Integer FK field',
    String VARCHAR(255) COMMENT 'String field',
    String VARCHAR(255) COMMENT 'String field',
    Integer VARCHAR(255) COMMENT 'Integer field',
    String VARCHAR(255) COMMENT 'String field',
    Text VARCHAR(255) COMMENT 'Text field',
    status VARCHAR(50) NOT NULL DEFAULT 'ACTIVE' COMMENT 'Entity status (ACTIVE, INACTIVE, SUSPENDED)',
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT 'Record creation timestamp',
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT 'Last update timestamp',
    version BIGINT NOT NULL DEFAULT 0 COMMENT 'Optimistic locking version'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='SystemLog entity table';

CREATE INDEX IF NOT EXISTS idx_system_log_created_at ON system_log (created_at) COMMENT 'Index for time-based queries';
CREATE INDEX IF NOT EXISTS idx_system_log_status ON system_log (status) COMMENT 'Index for status filtering';

-- ============================================
-- Table: performance_metric (PerformanceMetric)
-- ============================================
CREATE TABLE IF NOT EXISTS performance_metric (
    id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY COMMENT 'Primary Key',
    Integer PK VARCHAR(255) COMMENT 'Integer PK field',
    Float VARCHAR(255) COMMENT 'Float field',
    String VARCHAR(255) COMMENT 'String field',
    DateTime VARCHAR(255) COMMENT 'DateTime field',
    String VARCHAR(255) COMMENT 'String field',
    Float VARCHAR(255) COMMENT 'Float field',
    status VARCHAR(50) NOT NULL DEFAULT 'ACTIVE' COMMENT 'Entity status (ACTIVE, INACTIVE, SUSPENDED)',
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT 'Record creation timestamp',
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT 'Last update timestamp',
    version BIGINT NOT NULL DEFAULT 0 COMMENT 'Optimistic locking version'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='PerformanceMetric entity table';

CREATE INDEX IF NOT EXISTS idx_performance_metric_created_at ON performance_metric (created_at) COMMENT 'Index for time-based queries';
CREATE INDEX IF NOT EXISTS idx_performance_metric_status ON performance_metric (status) COMMENT 'Index for status filtering';

-- ============================================
-- Table: contrat_maintenance (ContratMaintenance)
-- ============================================
CREATE TABLE IF NOT EXISTS contrat_maintenance (
    id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY COMMENT 'Primary Key',
    Integer PK VARCHAR(255) COMMENT 'Integer PK field',
    Integer FK VARCHAR(255) COMMENT 'Integer FK field',
    DateTime VARCHAR(255) COMMENT 'DateTime field',
    DateTime VARCHAR(255) COMMENT 'DateTime field',
    Integer VARCHAR(255) COMMENT 'Integer field',
    Integer VARCHAR(255) COMMENT 'Integer field',
    Integer VARCHAR(255) COMMENT 'Integer field',
    Integer VARCHAR(255) COMMENT 'Integer field',
    status VARCHAR(50) NOT NULL DEFAULT 'ACTIVE' COMMENT 'Entity status (ACTIVE, INACTIVE, SUSPENDED)',
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT 'Record creation timestamp',
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT 'Last update timestamp',
    version BIGINT NOT NULL DEFAULT 0 COMMENT 'Optimistic locking version'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='ContratMaintenance entity table';

CREATE INDEX IF NOT EXISTS idx_contrat_maintenance_created_at ON contrat_maintenance (created_at) COMMENT 'Index for time-based queries';
CREATE INDEX IF NOT EXISTS idx_contrat_maintenance_status ON contrat_maintenance (status) COMMENT 'Index for status filtering';

-- ============================================
-- Table: draft_session (DraftSession)
-- ============================================
CREATE TABLE IF NOT EXISTS draft_session (
    id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY COMMENT 'Primary Key',
    Integer PK VARCHAR(255) COMMENT 'Integer PK field',
    Integer FK VARCHAR(255) COMMENT 'Integer FK field',
    Integer FK VARCHAR(255) COMMENT 'Integer FK field',
    String VARCHAR(255) COMMENT 'String field',
    JSON VARCHAR(255) COMMENT 'JSON field',
    DateTime VARCHAR(255) COMMENT 'DateTime field',
    String VARCHAR(255) COMMENT 'String field',
    Boolean VARCHAR(255) COMMENT 'Boolean field',
    Integer VARCHAR(255) COMMENT 'Integer field',
    status VARCHAR(50) NOT NULL DEFAULT 'ACTIVE' COMMENT 'Entity status (ACTIVE, INACTIVE, SUSPENDED)',
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT 'Record creation timestamp',
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT 'Last update timestamp',
    version BIGINT NOT NULL DEFAULT 0 COMMENT 'Optimistic locking version'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='DraftSession entity table';

CREATE INDEX IF NOT EXISTS idx_draft_session_created_at ON draft_session (created_at) COMMENT 'Index for time-based queries';
CREATE INDEX IF NOT EXISTS idx_draft_session_status ON draft_session (status) COMMENT 'Index for status filtering';

-- ============================================
-- Table: editor_state (EditorState)
-- ============================================
CREATE TABLE IF NOT EXISTS editor_state (
    id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY COMMENT 'Primary Key',
    Integer PK VARCHAR(255) COMMENT 'Integer PK field',
    Integer FK VARCHAR(255) COMMENT 'Integer FK field',
    Integer FK VARCHAR(255) COMMENT 'Integer FK field',
    JSON VARCHAR(255) COMMENT 'JSON field',
    Integer VARCHAR(255) COMMENT 'Integer field',
    DateTime VARCHAR(255) COMMENT 'DateTime field',
    Integer FK VARCHAR(255) COMMENT 'Integer FK field',
    status VARCHAR(50) NOT NULL DEFAULT 'ACTIVE' COMMENT 'Entity status (ACTIVE, INACTIVE, SUSPENDED)',
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT 'Record creation timestamp',
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT 'Last update timestamp',
    version BIGINT NOT NULL DEFAULT 0 COMMENT 'Optimistic locking version'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='EditorState entity table';

CREATE INDEX IF NOT EXISTS idx_editor_state_created_at ON editor_state (created_at) COMMENT 'Index for time-based queries';
CREATE INDEX IF NOT EXISTS idx_editor_state_status ON editor_state (status) COMMENT 'Index for status filtering';

-- ============================================
-- Table: offline_queue (OfflineQueue)
-- ============================================
CREATE TABLE IF NOT EXISTS offline_queue (
    id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY COMMENT 'Primary Key',
    Integer PK VARCHAR(255) COMMENT 'Integer PK field',
    Integer FK VARCHAR(255) COMMENT 'Integer FK field',
    String VARCHAR(255) COMMENT 'String field',
    JSON VARCHAR(255) COMMENT 'JSON field',
    DateTime VARCHAR(255) COMMENT 'DateTime field',
    Boolean VARCHAR(255) COMMENT 'Boolean field',
    Integer VARCHAR(255) COMMENT 'Integer field',
    String VARCHAR(255) COMMENT 'String field',
    status VARCHAR(50) NOT NULL DEFAULT 'ACTIVE' COMMENT 'Entity status (ACTIVE, INACTIVE, SUSPENDED)',
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT 'Record creation timestamp',
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT 'Last update timestamp',
    version BIGINT NOT NULL DEFAULT 0 COMMENT 'Optimistic locking version'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='OfflineQueue entity table';

CREATE INDEX IF NOT EXISTS idx_offline_queue_created_at ON offline_queue (created_at) COMMENT 'Index for time-based queries';
CREATE INDEX IF NOT EXISTS idx_offline_queue_status ON offline_queue (status) COMMENT 'Index for status filtering';

-- ============================================
-- Table: recovery_point (RecoveryPoint)
-- ============================================
CREATE TABLE IF NOT EXISTS recovery_point (
    id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY COMMENT 'Primary Key',
    Integer PK VARCHAR(255) COMMENT 'Integer PK field',
    Integer FK VARCHAR(255) COMMENT 'Integer FK field',
    JSON VARCHAR(255) COMMENT 'JSON field',
    String VARCHAR(255) COMMENT 'String field',
    Boolean VARCHAR(255) COMMENT 'Boolean field',
    DateTime VARCHAR(255) COMMENT 'DateTime field',
    status VARCHAR(50) NOT NULL DEFAULT 'ACTIVE' COMMENT 'Entity status (ACTIVE, INACTIVE, SUSPENDED)',
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT 'Record creation timestamp',
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT 'Last update timestamp',
    version BIGINT NOT NULL DEFAULT 0 COMMENT 'Optimistic locking version'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='RecoveryPoint entity table';

CREATE INDEX IF NOT EXISTS idx_recovery_point_created_at ON recovery_point (created_at) COMMENT 'Index for time-based queries';
CREATE INDEX IF NOT EXISTS idx_recovery_point_status ON recovery_point (status) COMMENT 'Index for status filtering';

-- ============================================
-- Table: content_version (ContentVersion)
-- ============================================
CREATE TABLE IF NOT EXISTS content_version (
    id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY COMMENT 'Primary Key',
    Integer PK VARCHAR(255) COMMENT 'Integer PK field',
    Integer FK VARCHAR(255) COMMENT 'Integer FK field',
    Integer VARCHAR(255) COMMENT 'Integer field',
    JSON VARCHAR(255) COMMENT 'JSON field',
    Integer FK VARCHAR(255) COMMENT 'Integer FK field',
    DateTime VARCHAR(255) COMMENT 'DateTime field',
    Boolean VARCHAR(255) COMMENT 'Boolean field',
    String VARCHAR(255) COMMENT 'String field',
    Float VARCHAR(255) COMMENT 'Float field',
    status VARCHAR(50) NOT NULL DEFAULT 'ACTIVE' COMMENT 'Entity status (ACTIVE, INACTIVE, SUSPENDED)',
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT 'Record creation timestamp',
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT 'Last update timestamp',
    version BIGINT NOT NULL DEFAULT 0 COMMENT 'Optimistic locking version'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='ContentVersion entity table';

CREATE INDEX IF NOT EXISTS idx_content_version_created_at ON content_version (created_at) COMMENT 'Index for time-based queries';
CREATE INDEX IF NOT EXISTS idx_content_version_status ON content_version (status) COMMENT 'Index for status filtering';

-- ============================================
-- Table: audit_log (AuditLog)
-- ============================================
CREATE TABLE IF NOT EXISTS audit_log (
    id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY COMMENT 'Primary Key',
    Integer PK VARCHAR(255) COMMENT 'Integer PK field',
    Integer FK VARCHAR(255) COMMENT 'Integer FK field',
    String VARCHAR(255) COMMENT 'String field',
    Integer VARCHAR(255) COMMENT 'Integer field',
    JSON VARCHAR(255) COMMENT 'JSON field',
    JSON VARCHAR(255) COMMENT 'JSON field',
    DateTime VARCHAR(255) COMMENT 'DateTime field',
    String VARCHAR(255) COMMENT 'String field',
    String VARCHAR(255) COMMENT 'String field',
    status VARCHAR(50) NOT NULL DEFAULT 'ACTIVE' COMMENT 'Entity status (ACTIVE, INACTIVE, SUSPENDED)',
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT 'Record creation timestamp',
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT 'Last update timestamp',
    version BIGINT NOT NULL DEFAULT 0 COMMENT 'Optimistic locking version'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='AuditLog entity table';

CREATE INDEX IF NOT EXISTS idx_audit_log_created_at ON audit_log (created_at) COMMENT 'Index for time-based queries';
CREATE INDEX IF NOT EXISTS idx_audit_log_status ON audit_log (status) COMMENT 'Index for status filtering';

-- ============================================
-- Table: cache (Cache)
-- ============================================
CREATE TABLE IF NOT EXISTS cache (
    id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY COMMENT 'Primary Key',
    Integer PK VARCHAR(255) COMMENT 'Integer PK field',
    String VARCHAR(255) COMMENT 'String field',
    JSON VARCHAR(255) COMMENT 'JSON field',
    DateTime VARCHAR(255) COMMENT 'DateTime field',
    Integer VARCHAR(255) COMMENT 'Integer field',
    Integer VARCHAR(255) COMMENT 'Integer field',
    Integer VARCHAR(255) COMMENT 'Integer field',
    JSON VARCHAR(255) COMMENT 'JSON field',
    status VARCHAR(50) NOT NULL DEFAULT 'ACTIVE' COMMENT 'Entity status (ACTIVE, INACTIVE, SUSPENDED)',
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT 'Record creation timestamp',
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT 'Last update timestamp',
    version BIGINT NOT NULL DEFAULT 0 COMMENT 'Optimistic locking version'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='Cache entity table';

CREATE INDEX IF NOT EXISTS idx_cache_created_at ON cache (created_at) COMMENT 'Index for time-based queries';
CREATE INDEX IF NOT EXISTS idx_cache_status ON cache (status) COMMENT 'Index for status filtering';

-- ============================================
-- Table: archive (Archive)
-- ============================================
CREATE TABLE IF NOT EXISTS archive (
    id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY COMMENT 'Primary Key',
    Integer PK VARCHAR(255) COMMENT 'Integer PK field',
    Integer FK VARCHAR(255) COMMENT 'Integer FK field',
    Integer VARCHAR(255) COMMENT 'Integer field',
    JSON VARCHAR(255) COMMENT 'JSON field',
    DateTime VARCHAR(255) COMMENT 'DateTime field',
    Integer VARCHAR(255) COMMENT 'Integer field',
    String VARCHAR(255) COMMENT 'String field',
    status VARCHAR(50) NOT NULL DEFAULT 'ACTIVE' COMMENT 'Entity status (ACTIVE, INACTIVE, SUSPENDED)',
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT 'Record creation timestamp',
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT 'Last update timestamp',
    version BIGINT NOT NULL DEFAULT 0 COMMENT 'Optimistic locking version'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='Archive entity table';

CREATE INDEX IF NOT EXISTS idx_archive_created_at ON archive (created_at) COMMENT 'Index for time-based queries';
CREATE INDEX IF NOT EXISTS idx_archive_status ON archive (status) COMMENT 'Index for status filtering';

-- ============================================
-- Table: analytics_session (AnalyticsSession)
-- ============================================
CREATE TABLE IF NOT EXISTS analytics_session (
    id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY COMMENT 'Primary Key',
    Integer PK VARCHAR(255) COMMENT 'Integer PK field',
    String VARCHAR(255) COMMENT 'String field',
    Integer FK VARCHAR(255) COMMENT 'Integer FK field',
    String VARCHAR(255) COMMENT 'String field',
    String VARCHAR(255) COMMENT 'String field',
    JSON VARCHAR(255) COMMENT 'JSON field',
    Integer VARCHAR(255) COMMENT 'Integer field',
    String VARCHAR(255) COMMENT 'String field',
    String VARCHAR(255) COMMENT 'String field',
    Integer VARCHAR(255) COMMENT 'Integer field',
    DateTime VARCHAR(255) COMMENT 'DateTime field',
    DateTime VARCHAR(255) COMMENT 'DateTime field',
    status VARCHAR(50) NOT NULL DEFAULT 'ACTIVE' COMMENT 'Entity status (ACTIVE, INACTIVE, SUSPENDED)',
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT 'Record creation timestamp',
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT 'Last update timestamp',
    version BIGINT NOT NULL DEFAULT 0 COMMENT 'Optimistic locking version'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='AnalyticsSession entity table';

CREATE INDEX IF NOT EXISTS idx_analytics_session_created_at ON analytics_session (created_at) COMMENT 'Index for time-based queries';
CREATE INDEX IF NOT EXISTS idx_analytics_session_status ON analytics_session (status) COMMENT 'Index for status filtering';

-- ============================================
-- Table: region (Region)
-- ============================================
CREATE TABLE IF NOT EXISTS region (
    id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY COMMENT 'Primary Key',
    Integer PK VARCHAR(255) COMMENT 'Integer PK field',
    String VARCHAR(255) COMMENT 'String field',
    String VARCHAR(255) COMMENT 'String field',
    String VARCHAR(255) COMMENT 'String field',
    String VARCHAR(255) COMMENT 'String field',
    String VARCHAR(255) COMMENT 'String field',
    String VARCHAR(255) COMMENT 'String field',
    status VARCHAR(50) NOT NULL DEFAULT 'ACTIVE' COMMENT 'Entity status (ACTIVE, INACTIVE, SUSPENDED)',
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT 'Record creation timestamp',
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT 'Last update timestamp',
    version BIGINT NOT NULL DEFAULT 0 COMMENT 'Optimistic locking version'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='Region entity table';

CREATE INDEX IF NOT EXISTS idx_region_created_at ON region (created_at) COMMENT 'Index for time-based queries';
CREATE INDEX IF NOT EXISTS idx_region_status ON region (status) COMMENT 'Index for status filtering';

-- ============================================
-- Table: article_tag (ArticleTag)
-- ============================================
CREATE TABLE IF NOT EXISTS article_tag (
    id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY COMMENT 'Primary Key',
    Integer FK VARCHAR(255) COMMENT 'Integer FK field',
    Integer FK VARCHAR(255) COMMENT 'Integer FK field',
    DateTime VARCHAR(255) COMMENT 'DateTime field',
    Float VARCHAR(255) COMMENT 'Float field',
    status VARCHAR(50) NOT NULL DEFAULT 'ACTIVE' COMMENT 'Entity status (ACTIVE, INACTIVE, SUSPENDED)',
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT 'Record creation timestamp',
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT 'Last update timestamp',
    version BIGINT NOT NULL DEFAULT 0 COMMENT 'Optimistic locking version'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='ArticleTag entity table';

CREATE INDEX IF NOT EXISTS idx_article_tag_created_at ON article_tag (created_at) COMMENT 'Index for time-based queries';
CREATE INDEX IF NOT EXISTS idx_article_tag_status ON article_tag (status) COMMENT 'Index for status filtering';

-- ============================================
-- Table: partage_config (PartageConfig)
-- ============================================
CREATE TABLE IF NOT EXISTS partage_config (
    id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY COMMENT 'Primary Key',
    Integer PK VARCHAR(255) COMMENT 'Integer PK field',
    Integer FK VARCHAR(255) COMMENT 'Integer FK field',
    String VARCHAR(255) COMMENT 'String field',
    String VARCHAR(255) COMMENT 'String field',
    String VARCHAR(255) COMMENT 'String field',
    String VARCHAR(255) COMMENT 'String field',
    String VARCHAR(255) COMMENT 'String field',
    String VARCHAR(255) COMMENT 'String field',
    String VARCHAR(255) COMMENT 'String field',
    Integer VARCHAR(255) COMMENT 'Integer field',
    status VARCHAR(50) NOT NULL DEFAULT 'ACTIVE' COMMENT 'Entity status (ACTIVE, INACTIVE, SUSPENDED)',
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT 'Record creation timestamp',
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT 'Last update timestamp',
    version BIGINT NOT NULL DEFAULT 0 COMMENT 'Optimistic locking version'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='PartageConfig entity table';

CREATE INDEX IF NOT EXISTS idx_partage_config_created_at ON partage_config (created_at) COMMENT 'Index for time-based queries';
CREATE INDEX IF NOT EXISTS idx_partage_config_status ON partage_config (status) COMMENT 'Index for status filtering';

-- ============================================
-- Table: lien_partage (LienPartage)
-- ============================================
CREATE TABLE IF NOT EXISTS lien_partage (
    id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY COMMENT 'Primary Key',
    Integer PK VARCHAR(255) COMMENT 'Integer PK field',
    Integer FK VARCHAR(255) COMMENT 'Integer FK field',
    Integer FK VARCHAR(255) COMMENT 'Integer FK field',
    String VARCHAR(255) COMMENT 'String field',
    String VARCHAR(255) COMMENT 'String field',
    Integer VARCHAR(255) COMMENT 'Integer field',
    Integer VARCHAR(255) COMMENT 'Integer field',
    DateTime VARCHAR(255) COMMENT 'DateTime field',
    DateTime VARCHAR(255) COMMENT 'DateTime field',
    status VARCHAR(50) NOT NULL DEFAULT 'ACTIVE' COMMENT 'Entity status (ACTIVE, INACTIVE, SUSPENDED)',
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT 'Record creation timestamp',
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT 'Last update timestamp',
    version BIGINT NOT NULL DEFAULT 0 COMMENT 'Optimistic locking version'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='LienPartage entity table';

CREATE INDEX IF NOT EXISTS idx_lien_partage_created_at ON lien_partage (created_at) COMMENT 'Index for time-based queries';
CREATE INDEX IF NOT EXISTS idx_lien_partage_status ON lien_partage (status) COMMENT 'Index for status filtering';

-- ============================================
-- Table: notification (Notification)
-- ============================================
CREATE TABLE IF NOT EXISTS notification (
    id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY COMMENT 'Primary Key',
    Integer PK VARCHAR(255) COMMENT 'Integer PK field',
    Integer FK VARCHAR(255) COMMENT 'Integer FK field',
    String VARCHAR(255) COMMENT 'String field',
    Text VARCHAR(255) COMMENT 'Text field',
    String VARCHAR(255) COMMENT 'String field',
    String VARCHAR(255) COMMENT 'String field',
    Boolean VARCHAR(255) COMMENT 'Boolean field',
    DateTime VARCHAR(255) COMMENT 'DateTime field',
    DateTime VARCHAR(255) COMMENT 'DateTime field',
    JSON VARCHAR(255) COMMENT 'JSON field',
    status VARCHAR(50) NOT NULL DEFAULT 'ACTIVE' COMMENT 'Entity status (ACTIVE, INACTIVE, SUSPENDED)',
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT 'Record creation timestamp',
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT 'Last update timestamp',
    version BIGINT NOT NULL DEFAULT 0 COMMENT 'Optimistic locking version'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='Notification entity table';

CREATE INDEX IF NOT EXISTS idx_notification_created_at ON notification (created_at) COMMENT 'Index for time-based queries';
CREATE INDEX IF NOT EXISTS idx_notification_status ON notification (status) COMMENT 'Index for status filtering';

-- ============================================
-- Table: notification_preference (NotificationPreference)
-- ============================================
CREATE TABLE IF NOT EXISTS notification_preference (
    id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY COMMENT 'Primary Key',
    Integer PK VARCHAR(255) COMMENT 'Integer PK field',
    Integer FK VARCHAR(255) COMMENT 'Integer FK field',
    Boolean VARCHAR(255) COMMENT 'Boolean field',
    Boolean VARCHAR(255) COMMENT 'Boolean field',
    Boolean VARCHAR(255) COMMENT 'Boolean field',
    Boolean VARCHAR(255) COMMENT 'Boolean field',
    Boolean VARCHAR(255) COMMENT 'Boolean field',
    Boolean VARCHAR(255) COMMENT 'Boolean field',
    Boolean VARCHAR(255) COMMENT 'Boolean field',
    Boolean VARCHAR(255) COMMENT 'Boolean field',
    status VARCHAR(50) NOT NULL DEFAULT 'ACTIVE' COMMENT 'Entity status (ACTIVE, INACTIVE, SUSPENDED)',
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT 'Record creation timestamp',
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT 'Last update timestamp',
    version BIGINT NOT NULL DEFAULT 0 COMMENT 'Optimistic locking version'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='NotificationPreference entity table';

CREATE INDEX IF NOT EXISTS idx_notification_preference_created_at ON notification_preference (created_at) COMMENT 'Index for time-based queries';
CREATE INDEX IF NOT EXISTS idx_notification_preference_status ON notification_preference (status) COMMENT 'Index for status filtering';

-- ============================================
-- Table: push_notification (PushNotification)
-- ============================================
CREATE TABLE IF NOT EXISTS push_notification (
    id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY COMMENT 'Primary Key',
    Integer PK VARCHAR(255) COMMENT 'Integer PK field',
    Integer FK VARCHAR(255) COMMENT 'Integer FK field',
    String VARCHAR(255) COMMENT 'String field',
    JSON VARCHAR(255) COMMENT 'JSON field',
    DateTime VARCHAR(255) COMMENT 'DateTime field',
    DateTime VARCHAR(255) COMMENT 'DateTime field',
    String VARCHAR(255) COMMENT 'String field',
    status VARCHAR(50) NOT NULL DEFAULT 'ACTIVE' COMMENT 'Entity status (ACTIVE, INACTIVE, SUSPENDED)',
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT 'Record creation timestamp',
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT 'Last update timestamp',
    version BIGINT NOT NULL DEFAULT 0 COMMENT 'Optimistic locking version'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='PushNotification entity table';

CREATE INDEX IF NOT EXISTS idx_push_notification_created_at ON push_notification (created_at) COMMENT 'Index for time-based queries';
CREATE INDEX IF NOT EXISTS idx_push_notification_status ON push_notification (status) COMMENT 'Index for status filtering';

-- ============================================
-- Table: email_notification (EmailNotification)
-- ============================================
CREATE TABLE IF NOT EXISTS email_notification (
    id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY COMMENT 'Primary Key',
    Integer PK VARCHAR(255) COMMENT 'Integer PK field',
    Integer FK VARCHAR(255) COMMENT 'Integer FK field',
    String VARCHAR(255) COMMENT 'String field',
    String VARCHAR(255) COMMENT 'String field',
    Text VARCHAR(255) COMMENT 'Text field',
    Text VARCHAR(255) COMMENT 'Text field',
    DateTime VARCHAR(255) COMMENT 'DateTime field',
    DateTime VARCHAR(255) COMMENT 'DateTime field',
    status VARCHAR(50) NOT NULL DEFAULT 'ACTIVE' COMMENT 'Entity status (ACTIVE, INACTIVE, SUSPENDED)',
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT 'Record creation timestamp',
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT 'Last update timestamp',
    version BIGINT NOT NULL DEFAULT 0 COMMENT 'Optimistic locking version'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='EmailNotification entity table';

CREATE INDEX IF NOT EXISTS idx_email_notification_created_at ON email_notification (created_at) COMMENT 'Index for time-based queries';
CREATE INDEX IF NOT EXISTS idx_email_notification_status ON email_notification (status) COMMENT 'Index for status filtering';

-- ============================================
-- Table: notification_template (NotificationTemplate)
-- ============================================
CREATE TABLE IF NOT EXISTS notification_template (
    id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY COMMENT 'Primary Key',
    Integer PK VARCHAR(255) COMMENT 'Integer PK field',
    Integer FK VARCHAR(255) COMMENT 'Integer FK field',
    String VARCHAR(255) COMMENT 'String field',
    Text VARCHAR(255) COMMENT 'Text field',
    JSON VARCHAR(255) COMMENT 'JSON field',
    Boolean VARCHAR(255) COMMENT 'Boolean field',
    status VARCHAR(50) NOT NULL DEFAULT 'ACTIVE' COMMENT 'Entity status (ACTIVE, INACTIVE, SUSPENDED)',
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT 'Record creation timestamp',
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT 'Last update timestamp',
    version BIGINT NOT NULL DEFAULT 0 COMMENT 'Optimistic locking version'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='NotificationTemplate entity table';

CREATE INDEX IF NOT EXISTS idx_notification_template_created_at ON notification_template (created_at) COMMENT 'Index for time-based queries';
CREATE INDEX IF NOT EXISTS idx_notification_template_status ON notification_template (status) COMMENT 'Index for status filtering';

-- ============================================
-- Table: device_token (DeviceToken)
-- ============================================
CREATE TABLE IF NOT EXISTS device_token (
    id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY COMMENT 'Primary Key',
    Integer PK VARCHAR(255) COMMENT 'Integer PK field',
    Integer FK VARCHAR(255) COMMENT 'Integer FK field',
    String VARCHAR(255) COMMENT 'String field',
    String VARCHAR(255) COMMENT 'String field',
    Boolean VARCHAR(255) COMMENT 'Boolean field',
    DateTime VARCHAR(255) COMMENT 'DateTime field',
    DateTime VARCHAR(255) COMMENT 'DateTime field',
    status VARCHAR(50) NOT NULL DEFAULT 'ACTIVE' COMMENT 'Entity status (ACTIVE, INACTIVE, SUSPENDED)',
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT 'Record creation timestamp',
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT 'Last update timestamp',
    version BIGINT NOT NULL DEFAULT 0 COMMENT 'Optimistic locking version'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='DeviceToken entity table';

CREATE INDEX IF NOT EXISTS idx_device_token_created_at ON device_token (created_at) COMMENT 'Index for time-based queries';
CREATE INDEX IF NOT EXISTS idx_device_token_status ON device_token (status) COMMENT 'Index for status filtering';

