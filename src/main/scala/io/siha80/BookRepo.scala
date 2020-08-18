package io.siha80

import cats.effect.IO
import cats.implicits._

import io.siha80.model.{Book, BookWithId}
import io.siha80.model.BookModels.{BookId, Message}

import scala.collection.mutable

trait BookRepo {
  def addBook(book: Book): IO[BookId]
  def getBook(id: BookId): IO[Option[BookWithId]]
  def deleteBook(id: BookId): IO[Either[Message, Unit]]
  def updateBook(id: BookId, book: Book): IO[Either[Message, Unit]]
  def getBooks(): IO[List[BookWithId]]
}

object BookRepo {
  class DummyImpl extends BookRepo {
    val storage = mutable.HashMap[BookId, Book]().empty

    override def addBook(book: Book): IO[BookId] = IO {
      val bookId = BookId()
      storage.put(bookId, book)
      bookId
    }

    override def getBook(id: BookId): IO[Option[BookWithId]] = IO {
      storage.get(id).map(book => BookWithId(id.value, book.title, book.author))
    }

    override def deleteBook(id: BookId): IO[Either[Message, Unit]] = {
      for {
        removedBook <- IO(storage.remove(id))
        result = removedBook.toRight(s"Book with ${id.value} not found").void
      } yield result
    }

    override def updateBook(id: BookId, book: Book): IO[Either[Message, Unit]] = {
      for {
        bookOpt <- getBook(id)
        _ <- IO(bookOpt.toRight(s"Book with ${id.value} not found").void)
        updatedBook = storage.put(id, book).toRight(s"Book with ${id.value} not found").void
      } yield updatedBook
    }

    override def getBooks(): IO[List[BookWithId]] = IO {
      storage.toList.map(entry => BookWithId(entry._1.value, entry._2.title, entry._2.author))
    }
  }
}
