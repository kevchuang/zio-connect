package zio.connect.elasticsearch

import zio.Trace
import zio.connect.elasticsearch.ElasticsearchConnector.{ElasticsearchException, IndexKeyObject, IndexName}
import zio.prelude.Subtype
import zio.stream.{ZSink, ZStream}

trait ElasticsearchConnector {

  def deleteById(implicit trace: Trace): ZSink[Any, ElasticsearchException, IndexKeyObject, IndexKeyObject, Unit]

  def deleteIndex(implicit trace: Trace): ZSink[Any, ElasticsearchException, IndexName, IndexName, Unit]

  def exists(implicit trace: Trace): ZSink[Any, ElasticsearchException, IndexKeyObject, IndexKeyObject, Boolean]

  def get(indexKeyObject: => IndexKeyObject)(implicit trace: Trace): ZStream[Any, ElasticsearchException, Byte]

  def updateById(implicit trace: Trace): ZSink[Any, ElasticsearchException, IndexKeyObject, IndexKeyObject, Unit]

}

object ElasticsearchConnector {

  object IndexName extends Subtype[String]
  type IndexName = IndexName.Type

  object KeyId extends Subtype[String]
  type KeyId = KeyId.Type

  final case class IndexKeyObject(indexName: IndexName, keyId: KeyId)
  final case class ElasticsearchException(reason: Throwable)

}
