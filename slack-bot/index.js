const { WebClient } = require('@slack/web-api');
const { App } = require('@slack/bolt');
const { google } = require('googleapis');
const { auth } = require('google-auth-library');

const SLACK_BOT_TOKEN = 'xoxb-your-bot-token';
const SLACK_SIGNING_SECRET = 'your-signing-secret';
const GOOGLE_APPLICATION_CREDENTIALS = './path/to/your/service-account-key.json';

const slackClient = new WebClient(SLACK_BOT_TOKEN);

const app = new App({
  token: SLACK_BOT_TOKEN,
  signingSecret: SLACK_SIGNING_SECRET,
});

async function getCloudBuildLogs(buildId, projectId) {
  const client = await auth.getClient({
    keyFile: GOOGLE_APPLICATION_CREDENTIALS,
    scopes: ['https://www.googleapis.com/auth/cloud-platform'],
  });

  const cloudbuild = google.cloudbuild({
    version: 'v1',
    auth: client,
  });

  const res = await cloudbuild.projects.builds.get({
    projectId: projectId,
    id: buildId,
  });

  const steps = res.data.steps || [];
  const logs = steps.map((step, index) => `Step ${index + 1}: ${step.name}\n${step.logsBucket ? `Logs: ${step.logsBucket}` : ''}`).join('\n\n');

  return logs;
}

app.event('app_mention', async ({ event, context }) => {
  const command = event.text.split(' ');
  if (command[1] === 'logs') {
    const buildId = command[2];
    const projectId = command[3];

    try {
      const logs = await getCloudBuildLogs(buildId, projectId);
      if (logs) {
        await slackClient.chat.postMessage({
          channel: event.channel,
          text: 'Here are the Cloud Build logs:',
          blocks: [
            {
              type: 'section',
              text: {
                type: 'mrkdwn',
                text: '```' + logs + '```',
              },
            },
          ],
        });
      } else {
        await slackClient.chat.postMessage({
          channel: event.channel,
          text: 'No logs found. Please check your build ID and project ID.',
        });
      }
    } catch (error) {
      console.error('Error sending logs to Slack:', error);
    }
  }
});

(async () => {
  await app.start(process.env.PORT || 3000);
  console.log('Slackbot is running!');
})();
