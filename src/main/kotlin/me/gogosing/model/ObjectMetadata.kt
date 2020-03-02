package me.gogosing.model

/**
 * S3 Object Metadata.
 * SDK v1.x 는 별도의 metadata에 대한 wrapper 클래스를 제공하였으나
 * SDK v2.x 는 더 이상 제공하지 않으며, 각각의 meta를 Builder를 통하여 property로 제공하여야 함.
 * @see <a href="https://docs.aws.amazon.com/AmazonS3/latest/dev/UsingMetadata.html#object-metadata" target="_blank">
 *     object-metadata</a>
 */
data class ObjectMetadata(

    /**
     * 컨텐츠 유형.
     */
    var contentType: String,

    /**
     * 컨텐츠 길이.
     */
    var contentLength: Long
)
