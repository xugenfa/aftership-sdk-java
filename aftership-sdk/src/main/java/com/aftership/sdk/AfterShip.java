package com.aftership.sdk;

import com.aftership.sdk.endpoint.CheckpointEndpoint;
import com.aftership.sdk.endpoint.CourierEndpoint;
import com.aftership.sdk.endpoint.NotificationEndpoint;
import com.aftership.sdk.endpoint.TrackingEndpoint;
import com.aftership.sdk.error.AftershipException;
import com.aftership.sdk.error.ErrorMessage;
import com.aftership.sdk.impl.CheckpointImpl;
import com.aftership.sdk.impl.CourierImpl;
import com.aftership.sdk.impl.NotificationImpl;
import com.aftership.sdk.impl.TrackingImpl;
import com.aftership.sdk.lib.StrUtil;
import com.aftership.sdk.model.AftershipOption;
import com.aftership.sdk.model.RateLimit;
import com.aftership.sdk.rest.ApiRequest;
import com.aftership.sdk.rest.ApiRequestImpl;
import lombok.Getter;
import lombok.Setter;

/** Entry class Of API function */
@Getter
public class AfterShip {
  private static final String DEFAULT_ENDPOINT = "https://api.aftership.com/v4";
  private static final String DEFAULT_USER_AGENT = "aftership-sdk-java";

  /** apiKey parameter in API request */
  private final String apiKey;
  /** endpoint parameter in API request */
  private final String endpoint;
  /** userAgentPrefix parameter in API request */
  private final String userAgentPrefix;

  /** Status of Rate Limit */
  @Setter private RateLimit rateLimit;

  /** Endpoint of Courier */
  private final CourierEndpoint courierEndpoint;
  /** Endpoint of Tracking */
  private final TrackingEndpoint trackingEndpoint;
  /** Endpoint of Checkpoint */
  private final CheckpointEndpoint checkpointEndpoint;
  /** Endpoint of Notification */
  private final NotificationEndpoint notificationEndpoint;

  /**
   * Constructor
   *
   * @param apiKey apiKey parameter in API request
   */
  public AfterShip(String apiKey) {
    this(apiKey, null);
  }

  /**
   * Constructor
   *
   * @param apiKey apiKey parameter in API request
   * @param options Optional parameters for API request
   */
  public AfterShip(String apiKey, AftershipOption options) {
    if (StrUtil.isBlank(apiKey)) {
      throw new AftershipException(ErrorMessage.CONSTRUCTOR_INVALID_API_KEY);
    }

    this.apiKey = apiKey;

    // Setup
    if (options != null) {
      this.endpoint =
          StrUtil.isNotBlank(options.getEndpoint()) ? options.getEndpoint() : DEFAULT_ENDPOINT;
      this.userAgentPrefix =
          StrUtil.isNotBlank(options.getUserAgentPrefix())
              ? options.getUserAgentPrefix()
              : DEFAULT_USER_AGENT;
    } else {
      this.endpoint = DEFAULT_ENDPOINT;
      this.userAgentPrefix = DEFAULT_USER_AGENT;
    }

    this.rateLimit = new RateLimit(null, null, null);

    final ApiRequest request = new ApiRequestImpl(this);

    // Endpoints
    this.courierEndpoint = new CourierImpl(request);
    this.trackingEndpoint = new TrackingImpl(request);
    this.checkpointEndpoint = new CheckpointImpl(request);
    this.notificationEndpoint = new NotificationImpl(request);
  }
}