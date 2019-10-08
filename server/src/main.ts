import * as fs from 'fs';
import * as cors from 'cors';
import * as express from 'express';

import { NestFactory } from '@nestjs/core';
import { DocumentBuilder, SwaggerModule } from '@nestjs/swagger';
import { ExpressAdapter } from '@nestjs/platform-express';
import { AppModule } from './app.module';
import { INestApplication } from '@nestjs/common';

const corsOptions: cors.CorsOptions = {
  allowedHeaders: ['Origin', 'X-Requested-With', 'Content-Type', 'Accept', 'x-xsrf-token', 'x-auth-token'],
  credentials: true,
  methods: 'GET,HEAD,OPTIONS,PUT,PATCH,POST,DELETE',
  origin: ['*', 'http://localhost:4000'],
  preflightContinue: false,
  optionsSuccessStatus: 200,
};

export const initExpress = async () => {
  const server = express();
  server.disable('x-powered-by');
  server.use(cors(corsOptions));
  server.options('*', cors(corsOptions));

  server.use((err: any, _: express.Request, res: express.Response, next: express.NextFunction) => {
    res.status(500).send('Server error');
  });

  return server;
};

async function bootstrap() {
  const options = new DocumentBuilder()
    .setTitle('NestJS demo')
    .setDescription('DTO generator demo')
    .setVersion('1.0')
    .addTag('dto-generator')
    .build();

  const expressInstance = await initExpress();
  const server = new ExpressAdapter(expressInstance);

  const app: INestApplication = await NestFactory.create(AppModule, server, {});

  const document = SwaggerModule.createDocument(app, options);
  fs.writeFileSync('../open-api-schema.json', JSON.stringify(document));
  SwaggerModule.setup('/swagger', app, document);

  await app.listen(3000);
}
bootstrap();
