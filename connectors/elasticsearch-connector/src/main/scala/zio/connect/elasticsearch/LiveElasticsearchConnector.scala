package zio.connect.elasticsearch

import com.sksamuel.elastic4s.ElasticDsl.ExistsHandler
import zio.Chunk
//import com.sksamuel.elastic4s.ElasticDsl.DeleteByIdHandler
import com.sksamuel.elastic4s.http.JavaClient
import com.sksamuel.elastic4s.requests.exists.ExistsRequest
import com.sksamuel.elastic4s.zio.instances._
import com.sksamuel.elastic4s.{ElasticClient, ElasticProperties, Index}
import zio.{Trace, ZIO}
import zio.connect.elasticsearch.ElasticsearchConnector.{ElasticsearchException, IndexKeyObject, IndexName}
import zio.stream.{ZSink, ZStream}

final case class LiveElasticsearchConnector(client: ElasticClient) extends ElasticsearchConnector {

  override def deleteById(implicit
    trace: Trace
  ): ZSink[Any, ElasticsearchException, IndexKeyObject, IndexKeyObject, Unit] = {
    import com.sksamuel.elastic4s.ElasticDsl._
    ZSink
      .foreach[Any, Throwable, IndexKeyObject] { indexKey =>
        client.execute(
          deleteById(Index(indexKey.indexName), indexKey.keyId)
        )
      }
      .mapError(ElasticsearchException)
  }

  override def deleteIndex(implicit trace: Trace): ZSink[Any, ElasticsearchException, IndexName, IndexName, Unit] = {
    import com.sksamuel.elastic4s.ElasticDsl._
    ZSink
      .foreach[Any, Throwable, IndexName] { index =>
        client.execute(
          deleteIndex(index)
        )
      }
      .mapError(ElasticsearchException)
  }

  override def exists(implicit
    trace: Trace
  ): ZSink[Any, ElasticsearchException, IndexKeyObject, IndexKeyObject, Boolean] =
    ZSink
      .take[IndexKeyObject](1)
      .map(_.headOption)
      .mapZIO {
        case Some(indexKey) =>
          client
            .execute(
              ExistsRequest(indexKey.keyId, indexKey.indexName)
            )
            .map(_.result)
        case None => ZIO.succeed(false)
      }
      .mapError(ElasticsearchException)

  override def get(
    indexKeyObject: => IndexKeyObject
  )(implicit trace: Trace): ZStream[Any, ElasticsearchException, Byte] = {
    import com.sksamuel.elastic4s.ElasticDsl._
    ZStream
      .fromIterableZIO(
        client
          .execute(
            get(Index(indexKeyObject.indexName), indexKeyObject.keyId)
          )
          .map(response => Chunk.fromArray(response.result.sourceAsBytes))
      )
      .mapError(ElasticsearchException)
  }

  override def updateById(implicit
    trace: Trace
  ): ZSink[Any, ElasticsearchException, IndexKeyObject, IndexKeyObject, Unit] = {
    import com.sksamuel.elastic4s.ElasticDsl._
    ZSink
      .foreach[Any, ElasticsearchException, IndexKeyObject] { indexKey =>
        client
          .execute(
            updateById(Index(indexKey.indexName), indexKey.keyId)
          )
          .mapError(ElasticsearchException)
      }
  }

}
