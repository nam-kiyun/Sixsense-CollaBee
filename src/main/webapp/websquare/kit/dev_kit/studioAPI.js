window.studioAPI = {};

window.callbackApi = null;

studioAPI.enqueue = function (message, resolve, reject) {
  studioAPI.browserType = (
    navigator.userAgentData?.platform || navigator.platform
  )?.toLowerCase();
  studioAPI.osType = window.navigator.userAgent.toLocaleLowerCase();

  if (studioAPI.browserType.startsWith("win")) {
    if (!window.cefQuery) {
      console.warn("[window.cefQuery is not exist] action :: ", action);
      reject();
      return;
    }

    window.cefQuery({
      request: message,
      onSuccess: (res) => {
        $p.log("res", res);
        resolve(JSON.parse(res));
      },
      onFailure: (e) => {
        reject();
      },
    });
  } else {
    callWebSquareEditor(message);

    studioAPI.callbackApi = resolve
  }
};

studioAPI.webKitDispatch = function (requestData) {
    studioAPI.callbackApi(requestData)
};

studioAPI.dispatch = function (requestData) {
    // studioAPI.test123(requestData);
};

//callWebSquareEditor

studioAPI.exec = function (success, fail, message) {
  if (!success || {}.toString.call(success) !== "[object Function]") {
    success = function () {};
  }

  if (!fail || {}.toString.call(fail) !== "[object Function]") {
    fail = function () {};
  }
  studioAPI.enqueue(
    JSON.stringify({
      action: message.payload.type,
      msgType: message.type,
      req: { ...message.payload.req },
      res: {},
    }),
    success,
    fail
  );
};

studioAPI.promise = function (message) {
  return new Promise((resolve, reject) => {
    studioAPI.exec(resolve, reject, message);
  });
};
