package com.dorofeev.sandbox.reactidm.webapi;

import akka.actor.ActorRef;
import akka.actor.ActorSelection;
import akka.actor.ActorSystem;
import akka.pattern.Patterns;
import akka.util.Timeout;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import scala.concurrent.Future;
import scala.concurrent.duration.FiniteDuration;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.TimeUnit;

import static scala.compat.java8.FutureConverters.toJava;

@Component
public class PeopleServiceAPI {

	private final static Logger LOG = LoggerFactory.getLogger(PeopleServiceAPI.class);

	private ActorSystem system;
	private ActorRef managerRef;

	@PostConstruct
	public void postConstruct() throws Exception {

		system = ActorSystem.create("webapi");

		ActorSelection managerSelection = system.actorSelection("akka.tcp://people@127.0.0.1:2552/user/manager");
		managerSelection.resolveOneCS(new FiniteDuration(5, TimeUnit.SECONDS))
			.thenAccept(actorRef -> {
				if (actorRef == null) {
					LOG.error("Failed to resolve manager actor. Probably, this is due to different versions of Akka.");
					return;
				}

				managerRef = actorRef;
				LOG.info("Manager actor is resolved to " + managerRef.path());
			});
	}

	public CompletionStage<Void> createNewPerson(String firstName, String lastName) {
		String message = "createNewPerson: " + firstName + ", " + lastName;
		Future<Object> result = Patterns.ask(managerRef, message, new Timeout(1, TimeUnit.SECONDS));
		return toJava(result).thenApply(r -> null);
	}


	@PreDestroy
	public void terminate() {
		system.terminate();
		system.awaitTermination();
	}
}
