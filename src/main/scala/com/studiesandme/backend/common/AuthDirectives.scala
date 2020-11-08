package com.studiesandme.backend.common

import akka.http.scaladsl.model.headers.HttpChallenge
import akka.http.scaladsl.server.AuthenticationFailedRejection.{CredentialsMissing, CredentialsRejected}
import akka.http.scaladsl.server.{AuthenticationFailedRejection, Directive1, Directives, RequestContext}

trait AuthDirectives extends Directives {

  // See https://tools.ietf.org/html/rfc6750#section-3 for Bearer Token usage
  val challenge = HttpChallenge("Bearer", "api")

  def getAccessTokenFromContext(context: RequestContext): Option[AccessToken] = {
    val accessTokenFromHeader =
      context.request.headers
        .find(header => header.is("authorization") && header.value.startsWith("Bearer"))
        .flatMap { header =>
          AccessToken.fromString(header.value.replace("Bearer", "").trim)
        }

    val accessTokenFromQueryParameter =
      context.request.uri
        .query()
        .toMap
        .find(
          queryParameter =>
            List("authtoken", "accesstoken", "auth_token", "access_token").contains(queryParameter._1.toLowerCase),
        )
        .flatMap { queryParameter =>
          AccessToken.fromString(queryParameter._2)
        }

    accessTokenFromHeader orElse accessTokenFromQueryParameter
  }

  def extractAccessToken: Directive1[AccessToken] = extract(getAccessTokenFromContext).flatMap {
    case Some(accessToken) => provide(accessToken)
    case None              => reject(AuthenticationFailedRejection(CredentialsMissing, challenge))
  }

  @SuppressWarnings(Array("CatchThrowable"))
  def extractAuthInfo: Directive1[AuthInfo] = extract(getAccessTokenFromContext).flatMap {
    case Some(accessToken) => {
      try {
        provide(accessToken.authInfo)
      } catch {
        case _: Throwable => reject(AuthenticationFailedRejection(CredentialsRejected, challenge))
      }
    }
    case None => reject(AuthenticationFailedRejection(CredentialsMissing, challenge))
  }
}
