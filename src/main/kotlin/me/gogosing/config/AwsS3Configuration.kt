package me.gogosing.config

import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials
import software.amazon.awssdk.regions.Region
import software.amazon.awssdk.services.s3.S3Client

/**
 * AWS S3 configuration.
 */
@Configuration
class AwsS3Configuration(
    @Value("\${aws.access-key-id:}") val accessKeyId: String,
    @Value("\${aws.secret-access-key:}") val secretAccessKey: String
) {

    /**
     * AWS S3 Client 설정.
     */
    @Bean
    fun s3Client(): S3Client {
        return S3Client.builder()
            .credentialsProvider { AwsBasicCredentials.create(accessKeyId, secretAccessKey) }
            .region(Region.AP_NORTHEAST_2)
            .build()
    }
}
