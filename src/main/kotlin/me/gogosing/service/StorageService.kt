package me.gogosing.service

import me.gogosing.model.ObjectMetadata
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import software.amazon.awssdk.core.ResponseBytes
import software.amazon.awssdk.core.sync.RequestBody
import software.amazon.awssdk.core.sync.ResponseTransformer
import software.amazon.awssdk.services.s3.S3Client
import software.amazon.awssdk.services.s3.model.CopyObjectRequest
import software.amazon.awssdk.services.s3.model.GetObjectRequest
import software.amazon.awssdk.services.s3.model.GetObjectResponse
import software.amazon.awssdk.services.s3.model.PutObjectRequest
import java.net.URLEncoder
import java.nio.charset.StandardCharsets

/**
 * S3 파일 서비스 인터페이스.
 * Created by JinBum Jeong on 2020/03/02.
 */
interface StorageService {

    /**
     * S3 파일 업로드.
     * @param objectKey 파일이 저장될 경로.
     * @param byteArray 원본 파일의 byteArray
     * @param metadata 파일 metadata.
     */
    fun synchronizedFileUpload(objectKey: String, byteArray: ByteArray, metadata: ObjectMetadata)

    /**
     * S3 파일 복사.(존재하는 Object를 다른 버킷으로 복사.)
     * @param sourceObjectKey 복사 대상 Object key.
     * @param targetObjectKey 복사될 경로.
     */
    fun synchronizedFileCopy(sourceObjectKey: String, targetObjectKey: String)

    /**
     * S3 파일 다운로드.
     * @param objectKey 다운로드 대상 Object key.
     * @return 다운로드 응답.
     */
    fun synchronizedFileDownload(objectKey: String): ResponseBytes<GetObjectResponse>
}

@Service
class AwsS3ServiceImpl(
    @Value("\${aws.s3.bucket-name:}")
    private val bucket: String,
    private val s3Client: S3Client
) : StorageService {

    override fun synchronizedFileUpload(objectKey: String, byteArray: ByteArray, metadata: ObjectMetadata) {
        s3Client.putObject(
            PutObjectRequest.builder()
                .bucket(bucket)
                .key(objectKey)
                .contentType(metadata.contentType)
                .contentLength(metadata.contentLength)
                .build(),
            RequestBody.fromBytes(byteArray)
        )
    }

    override fun synchronizedFileCopy(sourceObjectKey: String, targetObjectKey: String) {
        s3Client.copyObject(
            CopyObjectRequest.builder()
                // bucket + key 형식이어야 하며, slash(/)로 구분되어야 하며, URL Encoded 되어야 한다.
                .copySource(URLEncoder.encode("$bucket/$sourceObjectKey", StandardCharsets.UTF_8))
                .bucket(bucket)
                .key(targetObjectKey)
                .build()
        )
    }

    override fun synchronizedFileDownload(objectKey: String): ResponseBytes<GetObjectResponse> {
        return s3Client.getObject(
            GetObjectRequest.builder().bucket(bucket).key(objectKey).build(),
            ResponseTransformer.toBytes()
        )
    }
}
