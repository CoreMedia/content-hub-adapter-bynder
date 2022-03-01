/** @type { import('@jangaroo/core').IJangarooConfig } */
module.exports = {
  type: "code",
  sencha: {
    name: "com.coremedia.labs.plugins__studio-client.content-hub-adapter-bynder",
    namespace: "com.coremedia.labs.plugins.adapters.bynder.client",
    studioPlugins: [
      {
        mainClass: "com.coremedia.labs.plugins.adapters.bynder.client.ContentHubStudioBynderPlugin",
        name: "Content Hub",
      },
    ],
  },
  command: {
    build: {
      ignoreTypeErrors: true
    },
  },
};
